/*
 * The MIT License
 *
 * Copyright 2013 Herald Studio, Southeast University.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package cn.edu.seu.herald.ws.dao.impl;

import cn.edu.seu.herald.ws.api.library.Book;
import org.springframework.util.Assert;

/**
 * Copyright (c) 2013 Ray <predator.ray@gmail.com>
 */
class BookParser {

    private String cnt1;
    private String cnt2;

    BookParser(String cnt1, String cnt2) {
        this.cnt1 = cnt1;
        this.cnt2 = cnt2;
    }

    public void parse(Book book) {
        parseCnt1(book);
        parseCnt2(book);
    }

    private static final String START_TAG = "<dd>";
    private static final String END_TAG = "</dd>";

    private void parseCnt1(Book book) {
        Assert.notNull(cnt1);
        Assert.notNull(book);

        String[] part = new String[3];
        String remain = cnt1;

        for (int i = 0; i < 3; ++i) {
            part[i] = getEachPart(remain);
            remain = getRemain(remain);
        }

        // [0]
        String name = part[0].substring(0, part[0].indexOf("</a>"));
        String author = part[0].substring(
                part[0].indexOf("</a>/") + "</a>/".length());
        book.setName(name);
        book.setAuthor(author);

        // [1]
        String press = part[1];
        book.setPress(press);

        // [2]
        String isbn = part[2].substring(0, part[2].indexOf("/"));
        book.setIsbn(isbn);
    }

    private void parseCnt2(Book book) {
        // TODO get copies info from cnt2, will be implement in M2
        book.setCopies(null);
    }

    private String getEachPart(String rawCnt) {
        int startOfPart = rawCnt.indexOf(START_TAG);
        int endOfPart = rawCnt.indexOf(END_TAG);
        checkStateOfIndices(startOfPart, endOfPart);

        // <dd>{part1}</dd>  =>  {part1}
        return rawCnt.substring(startOfPart + START_TAG.length(),
                endOfPart);
    }

    private String getRemain(String rawCnt) {
        int endOfPart = rawCnt.indexOf(END_TAG);
        // </dd>{remain}  =>  {remain}
        return rawCnt.substring(endOfPart + END_TAG.length());
    }

    private void checkStateOfIndices(int startOfPart, int endOfPart) {
        Assert.state(startOfPart != -1 && endOfPart != -1,
                "could not be parsed");
    }
}
