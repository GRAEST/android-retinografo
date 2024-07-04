package br.com.graest.retinografo.ui.screens.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.graest.retinografo.data.local.ExamDataDao
import br.com.graest.retinografo.data.model.ExamData
import br.com.graest.retinografo.utils.ImageConvertingUtils.bitmapToByteArray
import br.com.graest.retinografo.utils.ImageConvertingUtils.byteArrayToBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CameraViewModel(private val examDataDao: ExamDataDao): ViewModel() {
    private val _bitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val bitmaps = _bitmaps.asStateFlow()


    init {
        viewModelScope.launch {
            examDataDao.getExamData()
                .collect { entities ->
                    val loadedBitmaps = entities.map { entity ->
                        byteArrayToBitmap(entity.image)
                    }
                    _bitmaps.value = loadedBitmaps
                }
        }
    }

    fun onTakePhoto(bitmap: Bitmap) {

        viewModelScope.launch {
            val image = ExamData(image = bitmapToByteArray(bitmap))
            examDataDao.insertExam(image)
            // Update the _bitmaps state flow
            _bitmaps.value = _bitmaps.value + bitmap
        }
    }
}

