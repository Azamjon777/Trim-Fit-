package proleag.iyl.trimfitapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import proleag.iyl.trimfitapp.databinding.ItemProfileBinding
import proleag.iyl.trimfitapp.models.UserProfile

class ProfileAdapter(private val profiles: List<UserProfile>) :
    RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(profiles[position])
    }

    override fun getItemCount(): Int {
        return profiles.size
    }

    inner class ViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(profile: UserProfile) {
            binding.title.text = profile.title
            binding.desc.text = profile.desc
        }
    }
}

