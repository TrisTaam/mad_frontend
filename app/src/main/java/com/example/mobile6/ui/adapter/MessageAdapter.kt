import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile6.databinding.FragmentChatItemDoctorInDoctorListBinding
import com.example.mobile6.domain.model.Doctor

class MessageAdapter(
    private val onDoctorClick: (Doctor) -> Unit
) : RecyclerView.Adapter<MessageAdapter.DoctorViewHolder>() {

    private val doctors = mutableListOf<Doctor>()

    fun submitList(newList: List<Doctor>) {
        doctors.clear()
        doctors.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = FragmentChatItemDoctorInDoctorListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        holder.bind(doctors[position])
    }

    override fun getItemCount() = doctors.size

    inner class DoctorViewHolder(private val binding: FragmentChatItemDoctorInDoctorListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(doctor: Doctor) {
            binding.tvDoctorName.text = "${doctor.firstName} ${doctor.lastName}"
            binding.tvDoctorSpecialty.text = doctor.specialty
            // Nếu có avatarUrl thì load bằng Glide/Picasso vào binding.doctorAvatar
            binding.btnSelect.setOnClickListener { onDoctorClick(doctor) }
        }
    }
}