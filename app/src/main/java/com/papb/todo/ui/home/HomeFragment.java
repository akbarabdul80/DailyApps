package com.papb.todo.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.papb.todo.data.db.Sessions;
import com.papb.todo.data.model.label.DataLabel;
import com.papb.todo.data.model.label.ResponseLabel;
import com.papb.todo.data.model.task.ResponseTask;
import com.papb.todo.data.state.SimpleState;
import com.papb.todo.databinding.FragmentHomeBinding;
import com.papb.todo.root.App;
import com.papb.todo.ui.bottom_action.BottomActionFragment;
import com.papb.todo.ui.label.add.BottomAddFragment;
import com.papb.todo.ui.login.LoginActivity;
import com.papb.todo.ui.main.MainActivity;
import com.papb.todo.ui.main.MainViewModel;

public class HomeFragment extends Fragment implements LabelInterface {

    private FragmentHomeBinding binding;
    private LabelAdapter adapterLabel;
    private TaskHomeAdapter adapterTask;
    private MainViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new MainViewModel();
        initListener();

        // Setup Adapter
        adapterLabel = new LabelAdapter(this);
        adapterTask = new TaskHomeAdapter();

        binding.tvUsername.setText(App.session.getData(Sessions.name));
        binding.tvName.setText(App.session.getData(Sessions.email));

        // Setup RecyclerView
        binding.rvDataLabel.setAdapter(adapterLabel);
        binding.rvDataLabel.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        binding.rvDataTask.setAdapter(adapterTask);
        binding.rvDataTask.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.btnLogout.setOnClickListener(v -> new MaterialAlertDialogBuilder(getContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda ingin keluar dari akun ini?")
                .setPositiveButton("Ya", (dialogInterface, i) -> {
                    App.session.logout();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    ((Activity) getContext()).finish();
                }).setNegativeButton("Tidak", (dialogInterface, i) -> dialogInterface.dismiss())
                .show());

        BottomAddFragment addLabelBottom = new BottomAddFragment();
        binding.btnAddLabel.setOnClickListener( v -> addLabelBottom.show(getActivity().getSupportFragmentManager(), "Bottom Add Label"));
    }

    private void initListener() {
        viewModel.getStateGetDataLabel().observe(getViewLifecycleOwner(), simpleState -> {
            if (simpleState instanceof SimpleState.Loading) {
                binding.smDataLabel.setVisibility(View.VISIBLE);
                binding.rvDataLabel.setVisibility(View.GONE);
            } else if (simpleState instanceof SimpleState.Result) {
                binding.smDataLabel.setVisibility(View.GONE);
                binding.rvDataLabel.setVisibility(View.VISIBLE);
                if (((SimpleState.Result) simpleState).getData() instanceof ResponseLabel) {
                    ResponseLabel response = (ResponseLabel) ((SimpleState.Result<?>) simpleState).getData();
                    adapterLabel.submitData(response.getData());
                }
            } else if (simpleState instanceof SimpleState.Error) {
                binding.smDataLabel.setVisibility(View.VISIBLE);
                binding.rvDataLabel.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Gagal Mengambil data", Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getStateGetDataTask().observe(getViewLifecycleOwner(), simpleState -> {
            if (simpleState instanceof SimpleState.Loading) {
                binding.smDataTask.setVisibility(View.VISIBLE);
                binding.rvDataTask.setVisibility(View.GONE);
            } else if (simpleState instanceof SimpleState.Result) {
                binding.smDataTask.setVisibility(View.GONE);
                binding.rvDataTask.setVisibility(View.VISIBLE);
                if (((SimpleState.Result) simpleState).getData() instanceof ResponseTask) {
                    ResponseTask response = (ResponseTask) ((SimpleState.Result<?>) simpleState).getData();
                    adapterTask.submitData(response.getData());
                }
            } else if (simpleState instanceof SimpleState.Error) {
                binding.smDataTask.setVisibility(View.VISIBLE);
                binding.rvDataTask.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Gagal Mengambil data", Toast.LENGTH_LONG).show();
            }
        });
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getLabel();
        viewModel.getTaskToday();
    }

    @Override
    public void onCLick(DataLabel data) {

    }

    @Override
    public void onLongCLick(DataLabel data) {
        BottomActionFragment.Companion.newInstance(data).show(getParentFragmentManager(), "Bottom Action");
    }
}