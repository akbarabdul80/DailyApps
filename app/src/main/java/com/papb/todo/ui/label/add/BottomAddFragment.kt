package com.papb.todo.ui.label.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.oratakashi.viewbinding.core.binding.bottomsheet.viewBinding
import com.oratakashi.viewbinding.core.tools.onClick
import com.oratakashi.viewbinding.core.tools.toast
import com.papb.todo.R
import com.papb.todo.data.model.simple.ResponseSimple
import com.papb.todo.data.state.SimpleState
import com.papb.todo.databinding.FragmentBottomAddBinding
import com.papb.todo.ui.home.HomeInterface
import com.papb.todo.utils.Validation
import dmax.dialog.SpotsDialog

class BottomAddFragment : SuperBottomSheetFragment() {

    private val binding: FragmentBottomAddBinding by viewBinding()
    private val viewModel: AddLabelViewModel by viewModels()
    private val ivColor: MutableList<ImageView> = ArrayList()
    private var colorSelected: String = "red"
    private val spotsDialog: SpotsDialog by lazy {
        SpotsDialog(context, "Mohon tunggu...")
    }

    val parent: HomeInterface by lazy { context as HomeInterface }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        with(binding) {
            ivColor.addAll(listOf(ivRed, ivGreen, ivBlue, ivPurple))
            ivRed.onClick { onChangeColor("red") }
            ivGreen.onClick { onChangeColor("green") }
            ivBlue.onClick { onChangeColor("blue") }
            ivPurple.onClick { onChangeColor("purple") }

            btnSubmit.onClick {
                if (Validation.validateEditText(listOf(etName))) {
                    viewModel.addLabel(etName.text.toString(), colorSelected)
                }
            }
        }
    }

    private fun initListener() {
        viewModel.stateAddLabel.observe(this) {
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


    override fun getExpandedHeight(): Int {
        return -2
    }

    override fun isSheetAlwaysExpanded(): Boolean {
        return true
    }

}