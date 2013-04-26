/*
 * Copyright (C) 2012 René Jeschke <rene_jeschke@yahoo.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rjeschke.neetutils.json;

import java.io.IOException;
import java.io.Reader;

/**
 * JSON tokenizer.
 * 
 * @author René Jeschke (rene_jeschke@yahoo.de)
 * 
 */
public final class JSONTokenizer
{
    private final Reader        reader;
    private String              stringValue;
    private double              doubleValue;
    private long                longValue;
    private int                 current          = ' ';
    private int                 column           = 1;
    private int                 row              = 0;
    private final StringBuilder stringBuilder    = new StringBuilder(32);
    private final StringBuilder stringBuilderTmp = new StringBuilder(4);
    private Token               currentToken;

    protected JSONTokenizer(final Reader reader)
    {
        this.reader = reader;
    }

    /**
     * @return the last tokenized {@code double] value.

     */
    public double getDoubleValue()
    {
        return this.doubleValue;
    }

    /**
     * @return the last tokenized {@code long} value.
     */
    public long getLongValue()
    {
        return this.longValue;
    }

    /**
     * @return the last tokenized {@code String} value.
     */
    public String getStringValue()
    {
        return this.stringValue;
    }

    /**
     * 
     * @return the current {@link Token}
     */
    public Token getCurrentToken()
    {
        return this.currentToken;
    }

    private int read() throws IOException
    {
        this.current = this.reader.read();

        switch (this.current)
        {
        case '\n':
            this.row++;
            this.column = 0;
            break;
        case '\r':
            break;
        default:
            this.column++;
            break;
        }

        return this.current;
    }

    /**
     * @return the current parsing position as a
     *         {@code String] suitable for error reporting.

     */
    public String getPosition()
    {
        return " at row: " + this.row + ", column: " + this.column;
    }

    private Token readString() throws IOException
    {
        final StringBuilder sb = this.stringBuilder;
        sb.setLength(0);

        this.read();
        while (this.current != -1 && this.current != '"')
        {
            switch (this.current)
            {
            case '\\':
                this.read();
                switch (this.current)
                {
                case 'n':
                    sb.append('\n');
                    break;
                case 'r':
                    sb.append('\r');
                    break;
                case 't':
                    sb.append('\t');
                    break;
                case 'b':
                    sb.append('\b');
                    break;
                case 'f':
                    sb.append('\f');
                    break;
                case '/':
                    sb.append('/');
                    break;
                case '\\':
                    sb.append('\\');
                    break;
                case '"':
                    sb.append('"');
                    break;
                case 'u':
                    this.stringBuilderTmp.setLength(0);
                    for (int i = 0; i < 4; i++)
                    {
                        this.stringBuilderTmp.append((char)this.read());
                    }
                    try
                    {
                        sb.append((char)Integer.parseInt(this.stringBuilderTmp.toString(), 16));
                    }
                    catch (NumberFormatException e)
                    {
                        throw new IOException("Illegal unicode escape sequence" + this.getPosition(), e);
                    }
                    break;
                default:
                    throw new IOException("Illegal escape sequence" + this.getPosition());
                }
                break;
            default:
                sb.append((char)this.current);
                break;
            }
            this.read();
        }

        if (this.current != '"') throw new IOException("Unexpected end of data, open string" + this.getPosition());
        this.read();

        this.stringValue = sb.toString();

        return Token.STRING;
    }

    private Token readWord() throws IOException
    {
        final StringBuilder sb = this.stringBuilder;
        sb.setLength(0);
        sb.append((char)this.current);
        while (Character.isLetter((char)this.read()))
        {
            sb.append((char)this.current);
        }

        final String word = sb.toString();
        if (word.equals("true")) return Token.TRUE;
        if (word.equals("false")) return Token.FALSE;
        if (word.equals("null")) return Token.NULL;

        throw new IOException("Syntax error: " + word + "," + this.getPosition());
    }

    private Token readNumber() throws IOException
    {
        final StringBuilder sb = this.stringBuilder;
        boolean isDouble = false;
        sb.setLength(0);
        if (this.current == '-')
        {
            sb.append('-');
            this.read();
        }

        while (Character.isDigit((char)this.current))
        {
            sb.append((char)this.current);
            this.read();
        }

        if (this.current == '.')
        {
            isDouble = true;
            sb.append('.');
            this.read();
            while (Character.isDigit((char)this.current))
            {
                sb.append((char)this.current);
                this.read();
            }
        }

        if (this.current == 'e' || this.current == 'E')
        {
            isDouble = true;
            this.read();
            sb.append('e');
            if (this.current == '+' || this.current == '-')
            {
                sb.append((char)this.current);
                this.read();
            }
            while (Character.isDigit((char)this.current))
            {
                sb.append((char)this.current);
                this.read();
            }
        }

        try
        {
            if (isDouble)
            {
                this.doubleValue = Double.parseDouble(sb.toString());
                return Token.DOUBLE;
            }
            this.longValue = Long.parseLong(sb.toString());
            return Token.LONG;
        }
        catch (NumberFormatException e)
        {
            throw new IOException("Syntax error: " + sb.toString() + "," + this.getPosition(), e);
        }
    }

    /**
     * Parses the next {@link Token}.
     * 
     * @return The parsed {@code Token}
     * @throws IOException
     *             if an IO or parsing error occurred.
     */
    public Token next() throws IOException
    {
        for (;;)
        {
            switch (this.current)
            {
            case -1:
                return this.currentToken = Token.EOF;
            case '{':
                this.read();
                return this.currentToken = Token.OBJECT_OPEN;
            case '}':
                this.read();
                return this.currentToken = Token.OBJECT_CLOSE;
            case '[':
                this.read();
                return this.currentToken = Token.ARRAY_OPEN;
            case ']':
                this.read();
                return this.currentToken = Token.ARRAY_CLOSE;
            case ',':
                this.read();
                return this.currentToken = Token.COMMA;
            case ':':
                this.read();
                return this.currentToken = Token.COLON;
            case '"':
                return this.currentToken = this.readString();
            case '-':
                return this.currentToken = this.readNumber();
            default:
                if (Character.isWhitespace((char)this.current) || Character.isSpaceChar((char)this.current))
                {
                    this.read();
                    continue;
                }
                if (Character.isDigit((char)this.current))
                {
                    return this.currentToken = this.readNumber();
                }
                if (Character.isLetter((char)this.current))
                {
                    return this.currentToken = this.readWord();
                }
                throw new IOException("Illegal character: " + this.current + "," + this.getPosition());
            }
        }
    }

    /**
     * JSON token enum.
     * 
     * @author René Jeschke (rene_jeschke@yahoo.de)
     */
    public enum Token
    {
        OBJECT_OPEN, OBJECT_CLOSE, ARRAY_OPEN, ARRAY_CLOSE, COMMA, COLON, STRING, TRUE, FALSE, NULL, DOUBLE, LONG, EOF
    }
}
