package com.eunsong.receiptreader.data.source

import com.eunsong.receiptreader.data.Todo
import com.eunsong.receiptreader.data.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class DefaultTodoRepository @Inject constructor() : TodoRepository {
    override suspend fun save(todo: Todo) {
    }

    override fun findAll(): Flow<List<Todo>> {
        return flow {
            emit(listOf(Todo("", "", Date())))
        }
    }
}
