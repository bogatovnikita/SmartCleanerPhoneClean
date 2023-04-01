import android.content.Context
import com.example.permissions.hasStoragePermissions
import yin_kio.garbage_clean.domain.gateways.Permissions

class PermissionsImpl(
    private val context: Context
) : Permissions {

    override val hasPermission: Boolean
        get() = context.hasStoragePermissions()
}