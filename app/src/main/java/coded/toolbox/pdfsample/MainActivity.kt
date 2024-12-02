package coded.toolbox.pdfsample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val file = copyPDFToCache(context)
            file?.let {
                openPDF(context, it)
            }
        }) {
            Text(text = "Open PDF")
        }
    }
}

fun copyPDFToCache(context: android.content.Context): File? {
    return try {
        val inputStream = context.assets.open("sample.pdf")
        val cacheFile = File(context.cacheDir, "sample.pdf")

        val outputStream = FileOutputStream(cacheFile)
        inputStream.copyTo(outputStream)

        outputStream.flush()
        outputStream.close()
        inputStream.close()

        cacheFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun openPDF(context: android.content.Context, file: File) {
    val uri: Uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(
        Intent.createChooser(intent, "Open PDF with")
    )
}
