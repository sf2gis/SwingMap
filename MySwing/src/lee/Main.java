package lee;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;

public class Main {

	public static void main(String[] args) throws MalformedURLException,
			IOException {
		// TODO Auto-generated method stub
		// create data
		FileDataStore ds;
		ds = FileDataStoreFinder.getDataStore(new URL(
				"file://D:/Data/shapefiles/states.shp"));
		SimpleFeatureStore sfs;
		sfs = (SimpleFeatureStore) ds.getFeatureSource();

		MapContent map = new MapContent();
		map.setTitle("GtDemoXX");
		Style style = SLD.createLineStyle(Color.BLUE, 2);
		Layer layer = new FeatureLayer(sfs, style);
		map.addLayer(layer);

		// create gui
		JFrame f = new JFrame("MyPane");
		f.setLayout(new BorderLayout());
		f.setSize(300, 200);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final MyPanel mp = new MyPanel();

		mp.setMapContent(map);
		f.add(mp, BorderLayout.CENTER);
		System.out.println("show");
		//set pan tool
		mp.addMouseListener(new MouseAdapter() {// pan action
			private Point pos = null;

			@Override
			public void mousePressed(MouseEvent e) {// when click, remember
													// mouse pos
				// TODO Auto-generated method stub
				super.mousePressed(e);
				System.out.println("mousePressed()");
				Point posNow = e.getPoint();
				pos = posNow;
			}

			@Override
			public void mouseReleased(MouseEvent e) {// when release, calculate
														// new pos
				// TODO Auto-generated method stub
				super.mouseReleased(e);
				System.out.println("mouseReleased()");

				Point posNow = e.getPoint();// mouse position
				if (!pos.equals(posNow)) {
					Point ptMove = new Point(posNow.x - pos.x, posNow.y - pos.y);// move
																					// extent
					Point ptMapLocation = mp.getLocation();
					Point ptNew = new Point(ptMapLocation.x + ptMove.x,
							ptMapLocation.y + ptMove.y);// new position
					mp.setLocation(ptNew);
				}
			}
		});
	}
}
