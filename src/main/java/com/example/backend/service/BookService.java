package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.model.Book;
import com.example.backend.repository.BookRepository;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public Optional<Book> update(Long id, Book bookData) {
        return bookRepository.findById(id)
            .map(book -> {
                book.setTitle(bookData.getTitle());
                book.setAuthor(bookData.getAuthor());
                book.setYear(bookData.getYear());
                return bookRepository.save(book);
            });
    }

    public boolean delete(Long id) {
        return bookRepository.findById(id)
            .map(book -> {
                bookRepository.delete(book);
                return true;
            })
            .orElse(false);
    }
}

