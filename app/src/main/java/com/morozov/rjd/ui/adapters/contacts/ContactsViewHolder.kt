package com.morozov.rjd.ui.adapters.contacts

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.morozov.rjd.R
import com.morozov.rjd.mvp.models.ContactModel
import com.morozov.rjd.ui.adapters.listeners.OnItemClickListener
import kotlinx.android.synthetic.main.item_recycler.view.*
import java.text.SimpleDateFormat

class ContactsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    @SuppressLint("RestrictedApi")
    fun populate(contactModel: ContactModel, position: Int, listener: OnItemClickListener) {
        itemView.setOnClickListener {
            listener.onItemClick(itemView, position)
        }

        if (contactModel.photo != null) {
            val imageUri = contactModel.photo
            val imageStream = itemView.context.contentResolver?.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            itemView.imageCard.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, 90, 90, false))
            itemView.textLetter.visibility = View.GONE
            imageStream?.close()
        } else {
            itemView.imageCard.setImageBitmap(null)
            itemView.imageCard.setImageDrawable(ColorDrawable(itemView.context.resources.getColor(R.color.colorPrimary)))
            itemView.textLetter.visibility = View.VISIBLE
        }

        if (contactModel.name.isNotEmpty())
            itemView.textLetter.text = contactModel.name[0].toString()

        itemView.editName.hint = contactModel.name
        itemView.editFamily.hint = contactModel.family
        itemView.editSurname.hint = contactModel.surname
        itemView.editPhone.hint = contactModel.phoneNum

        if (contactModel.isFriend) {
            itemView.linearBirthday.visibility = View.VISIBLE
            itemView.linearPosition.visibility = View.GONE
            itemView.linearWorkPhone.visibility = View.GONE

            val dayMtYrFormat = SimpleDateFormat("dd/MM/yyyy")
            itemView.editBirthday.hint = dayMtYrFormat.format(contactModel.birthday)
        } else {
            itemView.linearBirthday.visibility = View.GONE
            itemView.linearPosition.visibility = View.VISIBLE
            itemView.linearWorkPhone.visibility = View.VISIBLE

            itemView.editPosition.hint = contactModel.position
            itemView.editWorkPhone.hint = contactModel.workPhone
        }
    }
}