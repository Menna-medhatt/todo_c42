package com.route.todoappc42.ui.screens.main.fragments.edit_task

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.route.todoappc42.database.MyDatabase
import com.route.todoappc42.databinding.FragmentEditBinding
import com.route.todoappc42.ui.model.Todo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var todo: Todo
    private val calendar: Calendar by lazy { Calendar.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false)

        todo = arguments?.getParcelable(KEY_TODO) ?: Todo(
            title = "", description = "", date = System.currentTimeMillis(), isDone = false
        )

        bindData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar.timeInMillis = todo.date
        bindData()
        binding.saveChanges.setOnClickListener { saveChanges() }
    }

    private fun bindData() {
        binding.title.setText(todo.title)
        binding.description.setText(todo.description)
        bindDate()
        bindTime()

        // DatePicker
        binding.selectDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    bindDate()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // TimePicker
        binding.selectTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                { _, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    bindTime()
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }


    private fun bindDate() {
        binding.selectDate.text = calendar.timeInMillis.toFormattedDate()
    }


    private fun bindTime() {
        binding.selectTime.text = calendar.timeInMillis.toFormattedTime()
    }

    private fun isValidForm(): Boolean {
        var isValid = true

        if (binding.title.text.toString().trim().isEmpty()) {
            binding.title.error = "Please enter valid title"
            isValid = false
        } else {
            binding.title.error = null
        }

        if (binding.description.text.toString().trim().isEmpty()) {
            binding.description.error = "Please enter valid description"
            isValid = false
        } else {
            binding.description.error = null
        }

        return isValid
    }

    private fun saveChanges() {
        if (!isValidForm()) return


        MyDatabase.getInstance(requireContext()).getTodoDao().updateTodo(
            todo.copy(
                title = binding.title.text.toString().trim(),
                description = binding.description.text.toString().trim(),
                date = calendar.timeInMillis
            )
        )

        parentFragmentManager.popBackStack()
    }

    private fun Long.toFormattedDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(java.util.Date(this))
    }

    private fun Long.toFormattedTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(java.util.Date(this))
    }

    companion object {
        private const val KEY_TODO = "todo"

        fun newInstance(todo: Todo): EditFragment {
            return EditFragment().apply {
                arguments = bundleOf(KEY_TODO to todo)
            }
        }
    }
}