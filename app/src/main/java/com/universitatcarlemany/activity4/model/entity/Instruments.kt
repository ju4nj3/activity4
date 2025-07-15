import android.os.Parcelable
import com.universitatcarlemany.activity4.model.entity.Instrument
import kotlinx.parcelize.Parcelize

@Parcelize
class Instruments(
    val items: MutableList<Instrument> = mutableListOf()
) : Parcelable {

    fun addItem(item: Instrument) {
        items.add(item)
    }

    fun removeItem(item: Instrument) {
        items.remove(item)
    }

    fun getAllItems(): List<Instrument> {
        return items
    }

    fun addAllItems(items: List<Instrument>) {
        this.items.addAll(items)
    }
}
