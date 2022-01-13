import androidx.annotation.WorkerThread
import com.example.musicplayer.radio.Radio
import com.example.musicplayer.radio.RadioDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class RadioRepository(private val radioDao: RadioDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val radioList: Flow<List<Radio>> = radioDao.getAll()

    // Workaround to run insert function in coroutine (DAO methods can't be suspended)
    fun insert(radio: Radio) {
        radioDao.insert(radio)
    }

    fun delete(radio: Radio){
        radioDao.delete(radio)
    }

    fun getRadioWithId(id: Long){
        radioDao.getById(id)
    }

    fun deleteById(id: Long){
        radioDao.deleteById(id)
    }

    fun getFirst() : Flow<List<Radio>> {
        return radioDao.getFirst()
    }

}