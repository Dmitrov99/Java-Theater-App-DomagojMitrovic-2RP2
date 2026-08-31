package hr.algebra.theaterapp_gui.service;

import hr.algebra.model.Theater;
import hr.algebra.theaterapp_gui.task.LoadDataTask;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class BasicService extends Service<List<Theater>> {
    @Override
    protected Task<List<Theater>> createTask() {
        return new LoadDataTask();
    }
}
