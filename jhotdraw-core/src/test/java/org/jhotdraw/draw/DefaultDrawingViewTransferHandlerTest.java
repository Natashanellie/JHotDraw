package org.jhotdraw.draw;

import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.io.OutputFormat;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultDrawingViewTransferHandlerTest {


    private DefaultDrawingViewTransferHandler handler;

    @BeforeMethod
    public void setUp() {
        handler = new DefaultDrawingViewTransferHandler();
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void testCreateTransferable_returnsNullWhenFormatsNull() {
        DrawingView view = mock(DrawingView.class);
        Drawing drawing = mock(Drawing.class);

        when(view.getDrawing()).thenReturn(drawing);
        when(drawing.getOutputFormats()).thenReturn(null);

        Transferable result = handler.createTransferable(view, new HashSet<>());

        assertNull(result);
    }

    @Test
    public void testCreateTransferable_returnsNullWhenFormatsEmpty() {
        DrawingView view = mock(DrawingView.class);
        Drawing drawing = mock(Drawing.class);

        when(view.getDrawing()).thenReturn(drawing);
        when(drawing.getOutputFormats()).thenReturn(new ArrayList<>());

        Transferable result = handler.createTransferable(view, new HashSet<>());

        assertNull(result);
    }

    // JUnit test for the best case scenario
    @Test
    public void testCreateTransferable_returnsTransferableWhenValid() throws Exception {

        //creates mock data
        DrawingView view = mock(DrawingView.class);
        Drawing drawing = mock(Drawing.class);
        when(view.getDrawing()).thenReturn(drawing);
        when(view.getScaleFactor()).thenReturn(1.0);

        Figure fig = mock(Figure.class);
        Set<Figure> figs = new HashSet<>();
        figs.add(fig);

        List<Figure> tobeCopied = new ArrayList<>();
        tobeCopied.add(fig);

        OutputFormat outputFormat1 = mock(OutputFormat.class);
        OutputFormat outputFormat2 = mock(OutputFormat.class);

        List<OutputFormat> outputFormats = new ArrayList<>();
        outputFormats.add(outputFormat1);
        outputFormats.add(outputFormat2);

        /*
        programs the drawings behavior by ensnuring that
        drawing.getoutputFormarts() and drawing.sort() is not null or empty
        */
        when(drawing.getOutputFormats()).thenReturn(outputFormats);
        when(drawing.sort(figs)).thenReturn(tobeCopied);

        Transferable t1 = mock(Transferable.class);
        Transferable t2 = mock(Transferable.class);

        DataFlavor flavor1 = DataFlavor.stringFlavor;
        DataFlavor flavor2 = DataFlavor.imageFlavor;

        when(t1.getTransferDataFlavors()).thenReturn(new DataFlavor[]{flavor1});
        when(t2.getTransferDataFlavors()).thenReturn(new DataFlavor[]{flavor2});

        when(outputFormat1.createTransferable(drawing, tobeCopied, 1.0)).thenReturn(t1);
        when(outputFormat2.createTransferable(drawing, tobeCopied, 1.0)).thenReturn(t2);

        // calling the method
        Transferable result = handler.createTransferable(view, figs);

        // Esnures the return is not null
        assertNotNull(result);

        //ensures the data formed in the method is the data that is returned
        assertTrue(result.isDataFlavorSupported(flavor1));
        assertTrue(result.isDataFlavorSupported(flavor2));

    }


}
