/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tpl.turtles.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

/**
 *
 * @author techplex
 */
public class Book {
	public static ItemStack createBook(List pages, String title, String author, boolean editable) {
		ItemStack book;
		if (editable) {
			book = new ItemStack(Material.BOOK_AND_QUILL);
		} else {
			book = new ItemStack(Material.WRITTEN_BOOK);
		}

		BookMeta bookMeta = (BookMeta) book.getItemMeta();

		bookMeta.setPages(pages);

		//set the title and author of this book
		bookMeta.setTitle(title);
		bookMeta.setAuthor(author);

		//update the ItemStack with this new meta
		book.setItemMeta(bookMeta);

		return book;
	}
	public static void give2player(Player p, ItemStack book) {
		p.getInventory().addItem(book);
	}
}
