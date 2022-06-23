package com.papb.todo.ui.bottom_action

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.oratakashi.viewbinding.core.binding.bottomsheet.viewBinding
import com.oratakashi.viewbinding.core.tools.onClick
import com.papb.todo.databinding.FragmentBottomActionBinding

class BottomActionFragment : SuperBottomSheetFragment() {

    private val binding: FragmentBottomActionBinding by viewBinding()
    private lateinit var parent: BottomAction
        private var data: Any? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getParcelable("data")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnEdit.onClick {
                parent.onUpdate(data!!)
                dismiss()
            }
            btnDelete.onClick {
                parent.onDelete(data!!)
                dismiss()
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(data: Any) =
            BottomActionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("data", data as Parcelable)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parent = context as BottomAction
    }

    interface BottomAction {
        fun onUpdate(data: Any)
        fun onDelete(data: Any)
    }

    override fun getExpandedHeight(): Int {
        return -2
    }

    override fun isSheetAlwaysExpanded(): Boolean {
        return true
    }
}