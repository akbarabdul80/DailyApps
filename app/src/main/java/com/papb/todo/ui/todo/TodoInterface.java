package com.papb.todo.ui.todo;

import com.papb.todo.data.model.task.DataTask;

public interface TodoInterface {
    void onCLick(DataTask data);
    void onLongCLick(DataTask data);
}
