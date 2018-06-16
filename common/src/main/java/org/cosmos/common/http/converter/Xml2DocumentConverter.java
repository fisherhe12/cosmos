package org.cosmos.common.http.converter;

import org.apache.http.client.ClientProtocolException;
import org.cosmos.common.http.converter.abs.AbstractStreamConverter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;


/**
 * <p>
 * Convert the http body to Document object.
 * </p>
 *
 * @author Robert Lee
 * @version 1.0
 * @since Jul 15, 2015
 */

public class Xml2DocumentConverter extends AbstractStreamConverter<Document> {

	@Override
	protected Document doConstructObject(InputStream is, Charset charset)
			throws IOException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
			return docBuilder.parse(is, charset.toString());
		} catch (ParserConfigurationException ex) {
			throw new IllegalStateException(ex);
		} catch (SAXException ex) {
			throw new ClientProtocolException("Malformed XML document", ex);
		}
	}

}
