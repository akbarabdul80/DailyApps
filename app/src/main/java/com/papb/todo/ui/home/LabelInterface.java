package com.papb.todo.ui.home;

import com.papb.todo.data.model.label.DataLabel;
import com.papb.todo.data.model.task.DataTask;

public interface LabelInterface {
    void onCLick(DataLabel data);
    void onLongCLick(DataLabel data);
}
