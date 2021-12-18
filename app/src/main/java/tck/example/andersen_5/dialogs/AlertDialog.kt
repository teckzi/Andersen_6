package tck.example.andersen_5.dialogs

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import tck.example.andersen_5.ContactRepository
import tck.example.andersen_5.fragments.ContactFragment
import java.util.*

fun deleteContactDialog(context: Context,id: UUID,returnBack:Boolean = false){
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Удалить контакт")
    builder.setCancelable(false)
    builder.setMessage("Вы уверены что хотите удалить контакт")
    builder.setPositiveButton("Да") { _, _ ->
        try {
            ContactRepository.get().deleteContact(id)
            if (returnBack) ContactFragment.callback?.onContactReplaced()
        } catch (e: Exception) {
            Log.d("TAG","deleteContactDialog exception")
        }
    }
    builder.setNegativeButton("Нет", null)
    val dialog: AlertDialog = builder.create()
    dialog.show()
}