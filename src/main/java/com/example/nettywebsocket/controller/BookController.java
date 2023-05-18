package com.example.nettywebsocket.controller;

import com.example.nettywebsocket.model.Book;
import com.example.nettywebsocket.dao.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {

  @Autowired
  private BookMapper bookMapper;

  @RequestMapping("/")
  public List<Book> home() {
    return bookMapper.findAll();
  }

  @RequestMapping("/books")
  public List<Book> books() {
    return home();
  }

  @RequestMapping("/books/{genre}")
  public List<Book> bookByIsbn( @PathVariable("genre") String genre) {
    return bookMapper.findByGenre(  genre);
  }
}
