package com.example.mystoryapp.view.storylist


import androidx.paging.PagingData
import com.example.mystoryapp.data.StoryListRepository
import com.example.mystoryapp.model.ListStoryItem
import com.example.mystoryapp.view.storylist.utils.DataDummy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit

class StoryListViewModelTest {

    @get:Rule
    val mockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var storyListRepository: StoryListRepository
    private lateinit var storyListViewModel: StoryListViewModel
    private val dummyStory = DataDummy.generateDummyStoryEntity() // Assuming this returns a ListStoryItem
    private val token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLVNBU2VXY0FUY19qa1ZmRDAiLCJpYXQiOjE2ODcyODc5MTh9.66cAWd-rM2AzMCFW63Ce2GWk0ob6Wxtl7O-AwRJG5rA"

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        MockitoAnnotations.initMocks(this)
        storyListViewModel = StoryListViewModel(storyListRepository)
    }

    @Test
    fun `when Get Story Should Not Null and Return Success`() = runBlocking {
        val expectedStory = PagingData.from(dummyStory)

        `when`(storyListRepository.getAllStories(token)).thenReturn(flow { emit(expectedStory) })

        val actualStory = storyListViewModel.getAllStory(token).toList()

        assertNotNull(actualStory)
        assertEquals(dummyStory.size, actualStory.size)

        assertEquals(dummyStory.first(), actualStory.first())
    }

    @Test
    fun `when No Story Should Return Empty List`() = runBlocking {
        val expectedStory = PagingData.empty<ListStoryItem>()

        `when`(storyListRepository.getAllStories(token)).thenReturn(flow { emit(expectedStory) })
        val actualStory = storyListViewModel.getAllStory(token).toList()

        assertTrue(actualStory.isEmpty())
    }
}
