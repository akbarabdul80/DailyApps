package com.papb.todo.ui.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.papb.todo.data.model.simple.ResponseSimple;
import com.papb.todo.data.model.task.DataTask;
import com.papb.todo.data.model.task.ResponseTask;
import com.papb.todo.data.state.SimpleState;
import com.papb.todo.databinding.FragmentTodoBinding;
import com.papb.todo.ui.bottom_action.BottomActionFragment;

public class TodoFragment extends Fragment implements TodoInterface {

    private TaskTodoAdapter adapter;

    private FragmentTodoBinding binding;
    private TodoViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new TodoViewModel();

        //Setup Adapter
        adapter = new TaskTodoAdapter(this);

        //Setup RecyclerView
        binding.rvData.setAdapter(adapter);
        binding.rvData.setLayoutManager(new LinearLayoutManager(getContext()));

        initListerner();
    }

    private void initListerner() {
        viewModel.getStateGetDataTask().observe(getViewLifecycleOwner(), simpleState -> {
            if (simpleState instanceof SimpleState.Loading) {
                binding.smDataTask.setVisibility(View.VISIBLE);
                binding.rvData.setVisibility(View.GONE);
            } else if (simpleState instanceof SimpleState.Result) {
                binding.smDataTask.setVisibility(View.GONE);
                binding.rvData.setVisibility(View.VISIBLE);
                if (((SimpleState.Result) simpleState).getData() instanceof ResponseTask) {
                    ResponseTask response = (ResponseTask) ((SimpleState.Result<?>) simpleState).getData();
                    adapter.submitData(response.getData());
                }
            } else if (simpleState instanceof SimpleState.Error) {
                binding.smDataTask.setVisibility(View.VISIBLE);
                binding.rvData.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Gagal Mengambil data", Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getStateUpdateDataTask().observe(getViewLifecycleOwner(), simpleState -> {
            if (simpleState instanceof SimpleState.Loading) {
                binding.smDataTask.setVisibility(View.VISIBLE);
                binding.rvData.setVisibility(View.GONE);
            } else if (simpleState instanceof SimpleState.Result) {
                binding.smDataTask.setVisibility(View.GONE);
                binding.rvData.setVisibility(View.VISIBLE);
                if (((SimpleState.Result) simpleState).getData() instanceof ResponseSimple) {
                    ResponseSimple response = (ResponseSimple) ((SimpleState.Result<?>) simpleState).getData();
                    if (response.getStatus()) {
                        viewModel.getTask();
                    } else {
                        Toast.makeText(getContext(), "Gagal Update Data", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (simpleState instanceof SimpleState.Error) {
                binding.smDataTask.setVisibility(View.VISIBLE);
                binding.rvData.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Gagal Mengambil data" + ((SimpleState.Error) simpleState).getError().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public TodoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTodoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getTask();
    }

    @Override
    public void onCLick(DataTask data) {
        data.setDone(!data.getDone());
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Konfirmasi")
                .setMessage((data.getDone()) ? "Apakan anda sudah menyelesaikan task ini?" : "Apakan anda ingin membatalkan checklist task ini?")
                .setPositiveButton("Ya", (dialogInterface, i) -> {
                    viewModel.updateTask(data);
                }).setNegativeButton("Tidak", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    @Override
    public void onLongCLick(DataTask data) {
        BottomActionFragment.Companion.newInstance(data).show(getParentFragmentManager(), "Bottom Action");
    }
}