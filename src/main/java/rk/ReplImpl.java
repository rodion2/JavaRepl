package rk;

import javarepl.client.EvaluationLog;
import javarepl.console.ConsoleConfig;
import javarepl.console.ConsoleLog;
import javarepl.console.ConsoleResult;
import javarepl.console.SimpleConsole;
import javarepl.console.commands.EvaluateFromHistory;
import javarepl.console.commands.ListValues;
import javarepl.console.commands.SearchHistory;
import javarepl.console.commands.ShowHistory;
import javarepl.console.rest.RestConsole;
import javarepl.internal.totallylazy.Sequence;
import org.springframework.stereotype.Component;
import rk.JavaRepl;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Date;

import static java.lang.System.getProperty;
import static javarepl.Evaluation.functions.expression;
import static javarepl.Result.result;
import static javarepl.client.EvaluationLog.Type.CONTROL;
import static javarepl.console.ConsoleConfig.consoleConfig;
import static javarepl.console.commands.ClearScreen.CLEAR_SCREEN_CMD;

@Component
public class ReplImpl implements JavaRepl {

    private RestConsole restConsole;

    @PostConstruct
    private void init() throws Exception {
        ConsoleConfig config = consoleConfig()
                .historyFile(new File(getProperty("user.home"), ".javarepl-embedded.history"))
                .commands(
                        ListValues.class,
                        ShowHistory.class,
                        EvaluateFromHistory.class,
                        SearchHistory.class)
                .results(
                        result("date", new Date()),
                        result("num", 42));

        restConsole = new RestConsole(new SimpleConsole(config), 8001);
        restConsole.start();
    }

    @Override
    public String execute(String s) throws Exception {
        ConsoleResult result = restConsole.execute(s);
        Sequence<ConsoleLog> logs = result.logs();
        StringBuilder builder = new StringBuilder();
        for(ConsoleLog log: logs){
            builder.append(log.message());
        }
        return builder.toString();
    }
}
