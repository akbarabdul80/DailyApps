package com.papb.todo.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.papb.todo.R;
import com.papb.todo.data.model.label.DataLabel;
import com.papb.todo.data.model.simple.ResponseSimple;
import com.papb.todo.data.model.task.DataTask;
import com.papb.todo.data.state.SimpleState;
import com.papb.todo.databinding.ActivityMainBinding;
import com.papb.todo.ui.bottom_action.BottomActionFragment;
import com.papb.todo.ui.home.HomeInterface;
import com.papb.todo.ui.label.edit.BottomEditFragment;
import com.papb.todo.ui.task.add.AddTaskActivity;
import com.papb.todo.ui.home.HomeFragment;
import com.papb.todo.ui.task.edit.EditTaskActivity;
import com.papb.todo.ui.todo.TodoFragment;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements BottomActionFragment.BottomAction, HomeInterface {

    private ActivityMainBinding binding;
    private SpotsDialog spotsDialog;
    private MainViewModel viewModel;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new MainViewModel();

        initListener();

        spotsDialog = new SpotsDialog(this, "Mohon Tunggu...");

        openFragment(new HomeFragment());
        binding.bnHome.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    openFragment(new HomeFragment());
                    return true;
                case R.id.nav_add:
                    startActivity(new Intent(this, AddTaskActivity.class));
                    return false;
                case R.id.nav_todo:
                    openFragment(new TodoFragment());
                    return true;
            }
            return false;
        });
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flHome, fragment)
                .commit();
    }

    private void initListener() {
        viewModel.getStateDeleteTask().observe(this, simpleState -> {
            if (simpleState instanceof SimpleState.Loading) {
                spotsDialog.show();
            } else if (simpleState instanceof SimpleState.Result) {
                spotsDialog.dismiss();
                if (((SimpleState.Result) simpleState).getData() instanceof ResponseSimple) {
                    openFragment(new TodoFragment());
                }
            } else if (simpleState instanceof SimpleState.Error) {
                spotsDialog.dismiss();
                Toast.makeText(this, "Terjadi kesalahan!", Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getStateDeleteLabel().observe(this, simpleState -> {
            if (simpleState instanceof SimpleState.Loading) {
                spotsDialog.show();
            } else if (simpleState instanceof SimpleState.Result) {
                spotsDialog.dismiss();
                if (((SimpleState.Result) simpleState).getData() instanceof ResponseSimple) {
                    openFragment(new HomeFragment());
                }
            } else if (simpleState instanceof SimpleState.Error) {
                spotsDialog.dismiss();
                Toast.makeText(this, "Terjadi kesalahan!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onUpdate(@NonNull Object data) {
        if (data instanceof DataTask) {
            startActivity(new Intent(getBaseContext(), EditTaskActivity.class).putExtra("data", ((DataTask) data)));
        }else if (data instanceof DataLabel) {
            BottomEditFragment.Companion.newInstance(((DataLabel) data)).show(getSupportFragmentManager(), "Bottom Action");
        }
    }

    @Override
    public void onDelete(@NonNull Object data) {
        if (data instanceof DataTask) {
            viewModel.deleteTask(String.valueOf(((DataTask) data).getId_task()));
        }else if (data instanceof DataLabel) {
            viewModel.deleteLabel(String.valueOf(((DataLabel) data).getId_label()));
        }
    }

    @Override
    public void onUpdate() {
        openFragment(new HomeFragment());
    }
}