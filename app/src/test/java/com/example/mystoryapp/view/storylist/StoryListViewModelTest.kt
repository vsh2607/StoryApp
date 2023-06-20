//package com.example.mystoryapp.view.storylist
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Observer
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import androidx.paging.PagingData
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.example.mystoryapp.data.StoryListRepository
//import com.example.mystoryapp.model.ListStoryItem
//import com.example.mystoryapp.utils.DataDummy
//import org.junit.Assert
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Rule
//
//import org.junit.Test
//import org.mockito.ArgumentMatchers.anyString
//import org.mockito.Mock
//import org.mockito.Mockito
//import org.mockito.Mockito.`when`
//import com.example.mystoryapp.utils.getOrAwaitValue
//import kotlinx.coroutines.flow.Flow
//import java.util.concurrent.CountDownLatch
//import java.util.concurrent.TimeUnit
//import java.util.concurrent.TimeoutException
//
//class StoryListViewModelTest {
//
//    @get:Rule
//    val instantExecutorRule = InstantTaskExecutorRule()
//
//    @Mock
//    private lateinit var storyListRepository: StoryListRepository
//    private lateinit var storyListViewModel: StoryListViewModel
//    private val dummyStoryList = DataDummy.generateDummyStoriesEntity()
//
//    @Before
//    fun setUp() {
//        storyListViewModel = StoryListViewModel(storyListRepository)
//    }
//
//    @Test
//    fun `when Get All Story Should Not Null and Return Success`() {
//        val expectedStoryList = Pager(PagingConfig(pageSize = 20)) {
//            FakeStoryListPagingSource(dummyStoryList)
//        }.flow
//        `when`(storyListRepository.getAllStories(anyString())).thenReturn(expectedStoryList)
//
//        val actualStoryList = storyListViewModel.getAllStory("dummy_token").getOrAwaitValue()
//
//        Mockito.verify(storyListRepository).getAllStories("dummy_token")
//        Assert.assertNotNull(actualStoryList)
//        Assert.assertTrue(actualStoryList is PagingData<ListStoryItem>)
//        Assert.assertEquals(dummyStoryList.size, actualStoryList.snapshot().size)
//    }
//
//    @Test
//    fun `when Empty Story List Should Return Empty Paging Data`() {
//        val emptyStoryList = Pager(PagingConfig(pageSize = 20)) {
//            FakeEmptyStoryListPagingSource()
//        }.flow
//        `when`(storyListRepository.getAllStories(anyString())).thenReturn(emptyStoryList)
//
//        val actualStoryList = storyListViewModel.getAllStory("dummy_token").getOrAwaitValue()
//
//        Mockito.verify(storyListRepository).getAllStories("dummy_token")
//        Assert.assertNotNull(actualStoryList)
//        Assert.assertTrue(actualStoryList is PagingData<ListStoryItem>)
//        Assert.assertTrue(actualStoryList.snapshot().isEmpty())
//    }
//
//    // Define your fake PagingSource implementations here
//
//    private class FakeStoryListPagingSource(private val storyList: List<ListStoryItem>) : PagingSource<Int, ListStoryItem>() {
//        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
//            val pageNumber = params.key ?: 1
//            val prevPageNumber = if (pageNumber > 1) pageNumber - 1 else null
//            val nextPageNumber = pageNumber + 1
//
//            return LoadResult.Page(
//                data = storyList,
//                prevKey = prevPageNumber,
//                nextKey = nextPageNumber
//            )
//        }
//
//        override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
//            return null
//        }
//    }
//
//    private class FakeEmptyStoryListPagingSource : PagingSource<Int, ListStoryItem>() {
//        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
//            return LoadResult.Page(
//                data = emptyList(),
//                prevKey = null,
//                nextKey = null
//            )
//        }
//
//        override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
//            return null
//        }
//    }
//
//    private fun <T> LiveData<T>.getOrAwaitValue(): T {
//        var value: T? = null
//        val latch = CountDownLatch(1)
//        val observer = object : Observer<T> {
//            override fun onChanged(t: T) {
//                value = t
//                latch.countDown()
//                this@getOrAwaitValue.removeObserver(this)
//            }
//        }
//        this.observeForever(observer)
//        latch.await(2, TimeUnit.SECONDS)
//
//        @Suppress("UNCHECKED_CAST")
//        return value as T
//    }
//}
//
