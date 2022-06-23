package com.papb.todo.ui.label.edit

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.oratakashi.viewbinding.core.binding.fragment.viewBinding
import com.oratakashi.viewbinding.core.tools.isNotNull
import com.oratakashi.viewbinding.core.tools.onClick
import com.oratakashi.viewbinding.core.tools.toast
import com.papb.todo.R
import com.papb.todo.data.model.label.DataLabel
import com.papb.todo.data.model.simple.ResponseSimple
import com.papb.todo.data.state.SimpleState
import com.papb.todo.databinding.FragmentBottomEditBinding
import com.papb.todo.ui.home.HomeInterface
import com.papb.todo.utils.Validation
import dmax.dialog.SpotsDialog

class BottomEditFragment : SuperBottomSheetFragment() {
    private val binding: FragmentBottomEditBinding by viewBinding()
    private val viewModel: EditLabelViewModel by viewModels()
    private val ivColor: MutableList<ImageView> = ArrayList()
    private var colorSelected: String = "red"
    private val spotsDialog: SpotsDialog by lazy {
        SpotsDialog(context, "Mohon tunggu...")
    }
    private var data: DataLabel? = null


    val parent: HomeInterface by lazy { context as HomeInterface }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        arguments?.let {
            data = it.getParcelable("data")
        }

        with(binding) {
            ivColor.addAll(listOf(ivRed, ivGreen, ivBlue, ivPurple))
            ivRed.onClick { onChangeColor("red") }
            ivGreen.onClick { onChangeColor("green") }
            ivBlue.onClick { onChangeColor("blue") }
            ivPurple.onClick { onChangeColor("purple") }

            if (data.isNotNull()){
                etName.setText(data!!.name_label)
                unCheckedAll()
                onChangeColor(data!!.color_label)
            }else{
                dismiss()
            }


            btnSubmit.onClick {
                if (Validation.validateEditText(listOf(etName))) {
                    viewModel.editLabel(data!!.id_label, etName.text.toString(), colorSelected)
                }
            }
        }
    }

    private fun initListener() {
        viewModel.stateEditLabel.observe(this) {
            when (it) {
                SimpleState.Loading -> {
                    spotsDialog.show()
                }
                is SimpleState.Result<*> -> {
                    spotsDialog.dismiss()
                    if (it.data is ResponseSimple) {
                        toast(it.data.message!!)
                        parent.onUpdate()
                        dismiss()
                    }
                }
                is SimpleState.Error -> {
                    spotsDialog.dismiss()
                }
            }
        }
    }

    private fun onChangeColor(color: String) {
        unCheckedAll()
        colorSelected = color
        when (color) {
            "red" -> {
                binding.ivRed.setImageResource(R.drawable.ic_check_color)
            }
            "green" -> {
                binding.ivGreen.setImageResource(R.drawable.ic_check_color)
            }
            "blue" -> {
                binding.ivBlue.setImageResource(R.drawable.ic_check_color)
            }
            "purple" -> {
                binding.ivPurple.setImageResource(R.drawable.ic_check_color)
            }
        }
    }

    private fun unCheckedAll() {
        ivColor.forEach {
            it.setImageResource(0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(data: DataLabel) =
            BottomEditFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("data", data as Parcelable)
                }
            }
    }


    override fun getExpandedHeight(): Int {
        return -2
    }

    override fun isSheetAlwaysExpanded(): Boolean {
        return true
    }

}