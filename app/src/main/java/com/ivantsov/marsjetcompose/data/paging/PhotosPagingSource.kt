package com.ivantsov.marsjetcompose.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ivantsov.marsjetcompose.data.model.PhotoItem
import com.ivantsov.marsjetcompose.data.reprository.PhotosRepository
import com.ivantsov.marsjetcompose.util.Outcome
import javax.inject.Inject

/**
 * The PhotosPagingSource is a class responsible for loading photos in a paginated manner from
 * a given PhotosRepository. It extends the PagingSource class and overrides
 * two methods: getRefreshKey and load.
 *
 * @property photosRepository The repository to fetch photos from.
 */
class PhotosPagingSource @Inject constructor(
    private val photosRepository: PhotosRepository,
    val page: Page = Page(DEFAULT_PAGE_SIZE)
) : PagingSource<Int, PhotoItem>() {
    /**
     * Returns null since we don't need to refresh the data.
     */
    override fun getRefreshKey(state: PagingState<Int, PhotoItem>): Int? = null

    /**
     *
     * The load method loads a page of photos from the repository using the given load parameters.
     * It first extracts the current page key from the load parameters. Then it fetches the photos
     * from the repository using the getPhotos method of the PhotosRepository.
     * If the fetch is successful, it takes the first 10 photos from the list and returns them
     * in a LoadResult.Page. The prevKey and nextKey parameters of the LoadResult.Page are set
     * based on whether there are previous or next pages available. If there is an exception while
     * fetching the photos, it returns a LoadResult.Error containing the exception.
     *
     * @param params The load parameters, which include the current page key.
     * @return A [LoadResult.Page] containing the loaded photos, along with the previous and next page keys.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoItem> {
        val currentPage = params.key ?: 0
        return try {
            when (val response = photosRepository.getPhotos()) {
                is Outcome.Failure -> {
                    LoadResult.Error(response.throwable!!) //todo: need to change it
                }
                is Outcome.Success -> {
                    val photoItems: List<PhotoItem> = response.value
                    val items = photoItems.take(page.size)

                    LoadResult.Page(
                        data = items,
                        prevKey = if (currentPage > 0) currentPage - 1 else null,
                        nextKey = if (items.isNotEmpty()) currentPage + 1 else null
                    )
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 10
    }
}

@JvmInline
value class Page(val size: Int)