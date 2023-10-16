package com.example.myittaroostockinventorymanger.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.myittaroostockinventorymanger.data.entities.Batch
import com.example.myittaroostockinventorymanger.data.entities.Item
import com.example.myittaroostockinventorymanger.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.Date

@RunWith(JUnit4::class)
@SmallTest
class DaoTest {

    private lateinit var database: Database
    private lateinit var dao: Dao

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            Database::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.dao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_insertItem() {
        val item = Item("Decogen")

        dao.insertItem(item).test().assertComplete()
        val itemList = dao.getAllItems().getOrAwaitValue()

        assertThat(itemList).contains(item)

    }

    @Test
    fun test_insertBatch() {
        val batch = Batch(1, 3.0, 30.0, 10, Date())

        dao.insertBatch(batch).test().assertComplete()
    }

    @Test
    fun test_getAllItems() {
        val item1 = Item("Paracetermol")
        val item2 = Item("Fluza")
        val item3 = Item("C-Vit")

        dao.insertItem(item1).test().assertComplete()
        dao.insertItem(item2).test().assertComplete()
        dao.insertItem(item3).test().assertComplete()
        val itemList = dao.getAllItems().getOrAwaitValue()
        assertThat(itemList.size).isEqualTo(3)
    }

    @Test
    fun test_deleteItem() {

        val item = Item("Decogen")

        dao.insertItem(item).test().assertComplete()
        var itemList = dao.getAllItems().getOrAwaitValue()
        dao.deleteItem(itemList[0]).test().assertComplete()
        itemList = dao.getAllItems().getOrAwaitValue()

        assertThat(itemList).isEmpty()
    }

    @Test
    fun test_updateItemName() {

        val item = Item("Decogen")

        dao.insertItem(item).test().assertComplete()
        var itemList = dao.getAllItems().getOrAwaitValue()
        var updateItem = itemList[0]
        updateItem.name = "Fluza"
        dao.updateItemName(updateItem).test().assertComplete()
        itemList = dao.getAllItems().getOrAwaitValue()
        assertThat(itemList[0].name).isEqualTo(updateItem.name)
    }

    @Test
    fun test_getAllItemNames() {

        val item1 = Item("Decogen")
        val item2 = Item("Fluza")

        dao.insertItem(item1).test().assertComplete()
        dao.insertItem(item2).test().assertComplete()

        val itemNameList = dao.getAllItemNames().getOrAwaitValue()
        assertThat(itemNameList).contains(item1.name)
        assertThat(itemNameList).contains(item2.name)
        assertThat(itemNameList.size).isEqualTo(2)
    }

    @Test
    fun test_findItemIdByName() {

        val item1 = Item("Decogen")
        val item2 = Item("Fluza")

        dao.insertItem(item1).test().assertComplete()
        dao.insertItem(item2).test().assertComplete()
        val itemList = dao.getAllItems().getOrAwaitValue()
        val item1Id = itemList[0].itemId
        val idList = dao.findItemIdByName(item1.name).test().values()
        assertThat(idList[0]).isEqualTo(item1Id)
    }

    @Test
    fun test_deleteItemsByIds() {

        val item1 = Item("Decogen")
        val item2 = Item("Fluza")
        val item3 = Item("C_Vit")

        dao.insertItem(item1).test().assertComplete()
        dao.insertItem(item2).test().assertComplete()
        dao.insertItem(item3).test().assertComplete()
        var itemList = dao.getAllItems().getOrAwaitValue()
        val idList = itemList.map { it.itemId }
        dao.deleteItemByIds(idList).test().assertComplete()
        itemList = dao.getAllItems().getOrAwaitValue()
        assertThat(itemList).isEmpty()
    }

    @Test
    fun test_searchItems() {

        val item1 = Item("Decogen")
        val item2 = Item("Enervon_C")
        val item3 = Item("C_Vit")

        dao.insertItem(item1).test().assertComplete()
        dao.insertItem(item2).test().assertComplete()
        dao.insertItem(item3).test().assertComplete()

        val searchItemList = dao.searchItems("%e%").getOrAwaitValue()
        assertThat(searchItemList.size).isEqualTo(2)
    }

    @Test
    fun test_getItemById() {

        val item1 = Item("Decogen")
        val item2 = Item("Enervon_C")
        val item3 = Item("C_Vit")

        dao.insertItem(item1).test().assertComplete()
        dao.insertItem(item2).test().assertComplete()
        dao.insertItem(item3).test().assertComplete()
        val itemList = dao.getAllItems().getOrAwaitValue()
        val item = dao.getItemById(itemList[0].itemId).getOrAwaitValue()
        assertThat(item).isEqualTo(item1)
    }

    @Test
    fun test_findItemWithBatchById() {

        val item = Item("halogen")
        val batch = Batch(1, 100.0, 200.0, 10, Date())
        item.itemId = 1
        batch.batchId = 1

        dao.insertItem(item).test().assertComplete()
        dao.insertBatch(batch).test().assertComplete()

        val batchWithItem = dao.findItemWithBatchById(batch.batchId).getOrAwaitValue()

        assertThat(batchWithItem.item).isEqualTo(item)
        assertThat(batchWithItem.batch).isEqualTo(batch)
    }
}