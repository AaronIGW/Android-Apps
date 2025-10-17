package edu.wcupa.awilliams.booktracker.data

class OfflineBookRepository(private val dao: BookDao) : BookRepository {
    override fun getAll() = dao.getAll()
    override fun getById(id: Int) = dao.getById(id)
    override suspend fun add(book: Book) { dao.insert(book) }
    override suspend fun update(book: Book) { dao.update(book) }
    override suspend fun delete(book: Book) { dao.delete(book) }
}
