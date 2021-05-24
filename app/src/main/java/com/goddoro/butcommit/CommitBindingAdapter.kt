package com.goddoro.butcommit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.goddoro.butcommit.data.model.Commit
import com.goddoro.butcommit.databinding.ItemCommitBinding
import io.reactivex.Observable
import androidx.databinding.library.baseAdapters.BR
import io.reactivex.subjects.PublishSubject


class CommitBindingAdapter : RecyclerView.Adapter<CommitBindingAdapter.CommitViewHolder>(){

    private val onClickMemo: PublishSubject<Commit> = PublishSubject.create()
    val clickMemo: Observable<Commit> = onClickMemo


    private val diff = object : DiffUtil.ItemCallback<Commit>() {
        override fun areItemsTheSame(oldItem: Commit, newItem: Commit): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Commit, newItem: Commit): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diff)

    fun submitItems(items: List<Commit>?) {
        differ.submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCommitBinding.inflate(inflater, parent, false)
        return CommitViewHolder(binding)

    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CommitViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }


    inner class CommitViewHolder ( val binding : ItemCommitBinding ) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener {
                onClickMemo.onNext(differ.currentList[layoutPosition])
            }

        }

        fun bind ( item : Commit) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()


        }

    }

}

@BindingAdapter("app:recyclerview_commit_list")
fun RecyclerView.setCommitList(items : List<Commit>?){
    (adapter as? CommitBindingAdapter)?.run {
        this.submitItems(items)
    }
}