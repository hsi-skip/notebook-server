package com.oracle.notebook.service.impl;

import com.oracle.notebook.config.Constants;
import com.oracle.notebook.exception.InvalidRequestFormatException;
import com.oracle.notebook.exception.LanguageUnsupportedException;
import com.oracle.notebook.exception.TimeOutException;
import com.oracle.notebook.models.Execution;
import com.oracle.notebook.models.InterpreterRequest;
import com.oracle.notebook.service.InterpreterService;
import lombok.RequiredArgsConstructor;
import org.graalvm.polyglot.PolyglotException;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class InterpreterServiceImpl implements InterpreterService {

    private final Pattern pattern = Pattern.compile("%(\\w+)\\s+(.*)");
    private Map<String, Execution> sessionExecution = new HashMap<>();
    private String result;

    @Override
    public Execution execute(InterpreterRequest request) throws RuntimeException {

        Timer timer = new Timer(true);
        final Execution finalContext;
        Execution context = null;

        try {

            String language;
            String code;

            Matcher matcher = pattern.matcher(request.getCode());

            if (matcher.matches()) {
                language = matcher.group(1);
                code = matcher.group(2);
            } else {
                throw new InvalidRequestFormatException("Invalid request format, use format like '%<interpreter-name><whitespace><code>'");
            }

            boolean exist = Arrays.stream(Constants.LANGUAGES).anyMatch(language::equalsIgnoreCase);
            if (!exist) {
                throw new LanguageUnsupportedException("This language is unsupported, the only language suported is " + Constants.LANGUAGES);
            }

            context = this.sessionExecution.get(request.getSessionId());

            if (context == null) {
                context = new Execution(language);

                this.sessionExecution.put(request.getSessionId(), context);
            }


            context.getContext().enter();
            context.getOutputStream().reset();
            finalContext = context;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finalContext.getContext().close(true);
                }
            }, Constants.TIME_OUT_VALUE);

            String result = finalContext.getContext().eval(language, code).toString();
            this.prepareResult(finalContext, result);

        } catch (PolyglotException e) {

            if (e.isCancelled()) {
                sessionExecution.remove(request.getSessionId());
                throw new TimeOutException("Request taking too long to execute");
            }

            throw new RuntimeException(e.getMessage());

        } finally {

            if (context != null) {
                context.getContext().leave();
            }
            timer.cancel();
            timer.purge();
        }

        return finalContext;
    }

    private void prepareResult(Execution execution, String result) {

        result = Arrays.stream(new String[]{"undefined", "null"}).anyMatch(result::equalsIgnoreCase) ? "" : result;
        new PrintStream(execution.getOutputStream()).print(result);
    }

}
