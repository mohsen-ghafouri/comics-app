package com.mghafori.comics.network.interactor

import com.mghafori.comics.model.Comic
import com.mghafori.comics.model.DataState
import com.mghafori.comics.network.ComicService
import com.mghafori.comics.network.model.ComicDtoMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetComic(
    private val comicService: ComicService,
    private val comicDtoMapper: ComicDtoMapper
) {

    fun execute(
        comicId: Int?
    ): Flow<DataState<Comic>> = flow {
        try {
            emit(DataState.loading())
            if (comicId != null) {
                val comic = getComicById(comicId)
                emit(DataState.success(comic))
            } else {
                val comic = getCurrentComic()
                emit(DataState.success(comic))
            }
        } catch (e: Exception) {
            emit(DataState.error<Comic>(e.message ?: "Unknown Error"))
        }
    }

    private suspend fun getComicById(comicId: Int): Comic {
        return comicDtoMapper.mapToDomainModel(comicService.getById(comic_id = comicId))
    }

    private suspend fun getCurrentComic(): Comic {
        return comicDtoMapper.mapToDomainModel(comicService.getCurrent())
    }
}