package com.route.todoappc42.ui.screens.main.fragments.todo_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendar.core.Week
import com.kizitonwose.calendar.core.WeekDay
import com.kizitonwose.calendar.core.WeekDayPosition
import com.kizitonwose.calendar.core.atStartOfMonth
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.DaySize
import com.kizitonwose.calendar.view.WeekDayBinder
import com.kizitonwose.calendar.view.WeekHeaderFooterBinder
import com.route.todoappc42.R
import com.route.todoappc42.database.MyDatabase
import com.route.todoappc42.databinding.FragmentTodoListBinding
import com.route.todoappc42.ui.model.Todo
import com.route.todoappc42.ui.screens.main.fragments.edit_task.EditFragment
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class TodoListFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTodoListBinding
    private val adapter = TodosAdapter(emptyList())
    private var selectedDate = WeekDay(date = LocalDate.now(), position = WeekDayPosition.InDate)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTodosRecycler()
        refreshTodosList()
        initCalendarView()
    }

    private fun initCalendarView() {
        binding.calendarView.dayBinder = object : WeekDayBinder<DayViewHolder> {
            override fun bind(container: DayViewHolder, data: WeekDay) {
                container.day.text = data.date.dayOfMonth.toString()
                container.name.text = data.date.dayOfWeek.name.take(3)


                container.view.backgroundTintList = ContextCompat.getColorStateList(
                    requireContext(),
                    if (data.date == selectedDate.date) R.color.blue else R.color.white
                )

                container.view.setOnClickListener {
                    val oldDate = selectedDate
                    selectedDate = data
                    binding.calendarView.notifyDayChanged(oldDate)
                    binding.calendarView.notifyDayChanged(data)
                    refreshTodosList()
                }
            }

            override fun create(view: View) = DayViewHolder(view)
        }

        binding.calendarView.daySize = DaySize.FreeForm
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(12)
        val endMonth = currentMonth.plusMonths(12)
        val firstDayOfWeek = firstDayOfWeekFromLocale()

        binding.calendarView.setup(
            startMonth.atStartOfMonth(), endMonth.atStartOfMonth(), firstDayOfWeek
        )
        binding.calendarView.scrollToWeek(LocalDate.now())

        binding.calendarView.weekHeaderBinder = object : WeekHeaderFooterBinder<WeekViewHolder> {
            override fun bind(container: WeekViewHolder, data: Week) {
                container.week.text = "${data.days[0].date.month.name} ${data.days[0].date.year}"
            }

            override fun create(view: View) = WeekViewHolder(view)
        }
    }

    fun refreshTodosList() {

        val allTodos = MyDatabase.getInstance(requireContext()).getTodoDao().getAllTodos()


        val filteredTodos = allTodos.filter { todo ->
            val todoDate =
                Instant.ofEpochMilli(todo.date).atZone(ZoneId.systemDefault()).toLocalDate()

            todoDate == selectedDate.date
        }

        adapter.submitList(filteredTodos)
    }

    private fun initTodosRecycler() {
        adapter.itemClickListener = object : TodosAdapter.ItemClickListener {
            override fun onItemClick(todo: Todo) {

                val editFragment = EditFragment.newInstance(todo)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, editFragment).addToBackStack(null).commit()
            }

            override fun onDoneClick(todo: Todo) {

                MyDatabase.getInstance(requireContext()).getTodoDao().updateTodo(
                    todo.copy(isDone = !todo.isDone)
                )
                refreshTodosList()
            }

            override fun onDeleteClick(todo: Todo) {

                MyDatabase.getInstance(requireContext()).getTodoDao().deleteTodo(todo)
                refreshTodosList()
            }
        }

        binding.todosRecycler.adapter = adapter
    }
}