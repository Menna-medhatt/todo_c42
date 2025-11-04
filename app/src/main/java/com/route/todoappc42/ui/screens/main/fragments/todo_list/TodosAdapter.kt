package com.route.todoappc42.ui.screens.main.fragments.todo_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.route.todoappc42.databinding.ItemTodoBinding
import com.route.todoappc42.ui.model.Todo
import androidx.core.view.isVisible
import com.route.todoappc42.R

class TodosAdapter(var todos: List<Todo>) : Adapter<TodosAdapter.TodoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todos[position]
        holder.bind(todo)
    }

    override fun getItemCount(): Int = todos.size


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newsTodos: List<Todo>){
        todos = newsTodos
        notifyDataSetChanged()
    }


    var itemClickListener: ItemClickListener? = null

    interface ItemClickListener{
        fun onItemClick(todo: Todo)

        fun onDoneClick(todo: Todo)

        fun onDeleteClick(todo: Todo)
    }

     inner class TodoViewHolder(val binding: ItemTodoBinding) : ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.itemTodoTitle.text = todo.title
            binding.itemTodoTime.text = todo.description
            binding.dragItem.setOnClickListener {
                itemClickListener?.onItemClick(todo)
            }
            binding.icDone.setOnClickListener {
                itemClickListener?.onDoneClick(todo.copy(isDone = true))
            }
            binding.deleteView.setOnClickListener {
                itemClickListener?.onDeleteClick(todo)
            }
            if (todo.isDone) {
                binding.doneLabel.isVisible = true
                binding.icDone.isVisible = false
                binding.verticalView.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
                binding.itemTodoTitle.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
            } else {
                binding.doneLabel.isVisible = false
                binding.icDone.isVisible = true
                binding.verticalView.setBackgroundColor(
                    ContextCompat.getColor(binding.root.context, R.color.blue)
                )
                binding.itemTodoTitle.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.black)
                )
            }
          }
        }
    }
