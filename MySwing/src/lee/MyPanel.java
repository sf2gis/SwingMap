package lee;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;

public class MyPanel extends JPanel {

	private MapContent mapContent;
	private BufferedImage baseImage;
	private Graphics2D baseImageGraphics;
	
	public MapContent getMapContent() {
		return mapContent;
	}
	public void setMapContent(MapContent mapContent) {
		this.mapContent = mapContent;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
//		g.drawString("MyPanel", 100, 10);
		drawLayers(true);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(baseImage, 0, 0, null);
	}

	//draw map to buffer
	protected void drawLayers(boolean createNewImage) {
		Rectangle rect = getVisibleRect();
		System.out.println("getVisilbleRect="+rect);
		try {
			if (null == baseImage) {
				//create buff size use map bounds ratio
				ReferencedEnvelope mapArea = mapContent.getMaxBounds();
				System.out.println("ReferencedEnvelope="+mapArea);
				double dRatio=mapArea.getWidth()/mapArea.getHeight();
				double dRW=rect.height*dRatio;
				double dRH=rect.width/dRatio;
				if(dRW<=rect.width){
					rect.width=new Double(dRW+0.5).intValue();
				}else{
					rect.height=new Double(dRH+0.5).intValue();
				}
				System.out.println("radioed="+rect);
				//create buffer
				baseImage = GraphicsEnvironment
						.getLocalGraphicsEnvironment()
						.getDefaultScreenDevice()
						.getDefaultConfiguration()
						.createCompatibleImage(rect.width, rect.height,
								Transparency.TRANSLUCENT);
				//create buffer graphics
				baseImageGraphics = baseImage.createGraphics();
			}
			else{//when update
				baseImageGraphics.setBackground(getBackground());
				baseImageGraphics.clearRect(0, 0, rect.width, rect.height);
			}
			
			//render map
			ReferencedEnvelope mapArea = mapContent.getMaxBounds();
			Rectangle paintRect=new Rectangle(baseImage.getWidth(),baseImage.getHeight());
			GTRenderer renderer = new StreamingRenderer();
			renderer.setMapContent(mapContent);
			renderer.paint(baseImageGraphics, paintRect, mapArea);

		} finally {

		}
	}
}
