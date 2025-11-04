package com.route.todoappc42.ui.screens.add_todo

import android.app.DatePickerDialog

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.route.todoappc42.database.MyDatabase
import com.route.todoappc42.databinding.BottomSheetAddTodoBinding
import com.route.todoappc42.ui.model.Todo
import com.route.todoappc42.ui.utils.getFormattedDate
import com.route.todoappc42.ui.utils.getFormattedTime
import java.util.Calendar


class AddTodoBottomSheet(var onAddClick: ()->Unit) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetAddTodoBinding

    var selectedDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        bindDate()
        bindTime()
    }

    private fun bindDate() {
        binding.selectedDate.text = selectedDate.getFormattedDate()
    }

    private fun bindTime() {
        binding.selectedTime.text = selectedDate.getFormattedTime()
    }

    private fun initListeners() {
        binding.addBtn.setOnClickListener {
            if (!isValidForm()) return@setOnClickListener

            val todo = Todo(
                title = binding.titleInputLayout.editText!!.text.toString().trim(),
                description = binding.descriptionInputLayout.editText!!.text.toString().trim(),
                date = selectedDate.timeInMillis, isDone = false
            )
            MyDatabase.getInstance(requireContext())
            dismiss()
            onAddClick()
        }

        binding.selectedDate.setOnClickListener {
            val picker = DatePickerDialog(
                requireContext(), { p0, year, month, day ->
                    selectedDate[Calendar.YEAR] = year
                    selectedDate[Calendar.MONTH] = month
                    selectedDate[Calendar.DAY_OF_MONTH] = day
                    bindDate()
                },
                selectedDate[Calendar.YEAR],
                selectedDate[Calendar.MONTH],
                selectedDate[Calendar.DAY_OF_MONTH]
            )
            picker.show()
        }

        binding.selectedTime.setOnClickListener {
            val picker = TimePickerDialog(requireContext(),
                { p0, hour, minute ->
                    selectedDate[Calendar.HOUR_OF_DAY] = hour
                    selectedDate[Calendar.MINUTE] = minute
                    bindTime()
                }, selectedDate[Calendar.HOUR_OF_DAY], selectedDate[Calendar.MINUTE], true
            )
            picker.show()
        }

    }

    private fun isValidForm(): Boolean {
        var isValid = true
        if (binding.titleInputLayout.editText?.text.toString().isEmpty()) {
            binding.titleInputLayout.error = "Please enter valid title"
            //return false
            isValid = false
        } else {
            binding.titleInputLayout.error = null
        }

        if (binding.descriptionInputLayout.editText?.text.toString().isEmpty()) {
            binding.descriptionInputLayout.error = "Please enter valid description"
            // return false
            isValid = false
        } else {
            binding.descriptionInputLayout.error = null
        }
        return isValid;
    }
}
