package tck.example.andersen_5.contactsDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Contact(@PrimaryKey val id: UUID = UUID.randomUUID(),
                   var firstName: String = "",
                   var secondName:String = "",
                   var phoneNumber:String = "",
                   var photoUrl:String = "")