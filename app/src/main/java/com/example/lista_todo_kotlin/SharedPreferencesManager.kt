import android.content.Context

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor = sharedPreferences.edit()

    fun saveList(key: String, list: MutableList<String>) {
        val set = HashSet<String>(list)
        editor.putStringSet(key, set)
        editor.apply()
    }

    fun getList(key: String): MutableList<String> {
        val set = sharedPreferences.getStringSet(key, null)
        return set?.toMutableList() ?: mutableListOf<String>()
    }
}
