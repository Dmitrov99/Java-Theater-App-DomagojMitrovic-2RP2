package hr.algebra.theaterapp_gui.task;

import hr.algebra.model.Theater;
import javafx.concurrent.Task;

import java.util.List;

public class LoadDataTask extends Task<List<Theater>> {
    @Override
    protected List<Theater> call() throws Exception {
        return List.of();
    }
}
