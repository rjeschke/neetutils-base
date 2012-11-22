package com.github.rjeschke.neetutils;

import java.util.Arrays;

import com.github.rjeschke.neetutils.math.NMath;

public class CodePointIterator
{
    private final String string;
    private final int bufferSize;
    private final int bufferMask;
    private final int bufferNeeds;
    private final int buffer[];
    
    private final int lookBack;
    private final int lookAhead;
    
    private int bufferReadPos = 0;
    private int bufferWritePos = 0;
    private int stringPos = 0;
    
    public CodePointIterator(final String string, final int lookBack, final int lookAhead)
    {
        this.lookBack = lookBack;
        this.lookAhead = lookAhead;
        this.bufferNeeds = lookBack + lookAhead + 1;
        this.bufferSize = NMath.nextPow2(this.bufferNeeds);
        this.bufferMask = this.bufferSize - 1;
        this.buffer = new int[this.bufferSize];
        this.string = string;
     
        Arrays.fill(this.buffer, -1);
        this.bufferReadPos = 0;
        this.bufferWritePos = lookBack;
        
        this.fillBuffer();
    }
    
    public int available()
    {
        if(this.bufferReadPos <= this.bufferWritePos)
        {
            return this.bufferWritePos - this.bufferReadPos;
        }
        return this.bufferSize - this.bufferReadPos + this.bufferWritePos;
    }

    private int readBuffer(int offset)
    {
        return this.buffer[(this.bufferReadPos + this.lookBack + offset) & this.bufferMask];
    }
    
    private void fillBuffer()
    {
        final int todo = this.bufferNeeds - this.available();
        for(int i = 0; i < todo; i++)
        {
            final int ch;
            
            if(this.stringPos >= this.string.length())
            {
                ch = -1;
            }
            else
            {
                ch = this.string.codePointAt(this.stringPos);
                this.stringPos += ch < Character.MIN_SUPPLEMENTARY_CODE_POINT ? 1 : 2;
            }
            
            this.buffer[this.bufferWritePos] = ch;
            this.bufferWritePos = (this.bufferWritePos + 1) & this.bufferMask;
        }
    }
    
    public int get(int offset)
    {
        if(offset < -this.lookBack || offset > this.lookAhead)
            throw new IndexOutOfBoundsException("Got " + offset + ", range: " + (-this.lookBack) + " to " + this.lookAhead);
        return this.readBuffer(offset);
    }
    
    public int getCurrent()
    {
        return  this.readBuffer(0);
    }
    
    public void advance()
    {
        this.bufferReadPos = (this.bufferReadPos + 1) & this.bufferMask;
        this.fillBuffer();
    }
    
    public void advance(int numChars)
    {
        for(int i = 0; i < numChars; i++)
            this.advance();
    }
}
