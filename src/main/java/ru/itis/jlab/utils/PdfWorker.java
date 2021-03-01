package ru.itis.jlab.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lowagie.text.DocumentException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.jsoup.Jsoup;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Map;

public class PdfWorker {
//    static private String constStringForPdf = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";

    private String templatePath = "templates/";

    public String savePdf(Map<String, Object> data, String templatePdfFileName, String packageNamePdf) {
        String fileName = RandomStringUtils.random(5, true, true);
        String allFilePath = packageNamePdf + "/" + fileName + ".pdf";
        File folder = new File(packageNamePdf);
        if (!folder.exists()) {
            folder.mkdir();
        }
//        createPdf(data, allFilePath, templateFilePath);

        File templateHtmlFile = new File(templatePath + "html/" + templatePdfFileName + ".html");
        if (!templateHtmlFile.exists()) {
            File templatePdfFile = new File(templatePath + "pdf/" + templatePdfFileName + ".pdf");
            createHtmlByPdf(templatePdfFile, templatePdfFileName);
        }
        templateHtmlFile = new File(templatePath + "html/" + templatePdfFileName + ".html");
//        replaceAllInHtml(templateHtmlFile, data);
        createPdfByHtml(allFilePath, templatePath + "html/" + templatePdfFileName + ".html");
        return allFilePath;
    }

    //TODO: debug
    private void createPdfByHtml(String pdfFilePath, String pathTemplateHtmlFile) {
//        try {
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            // Форматирование HTML кода
//            /* эта процедура не обязательна, но я настоятельно рекомендую использовать этот блок */
//            HtmlCleaner cleaner = new HtmlCleaner();
//            CleanerProperties props = cleaner.getProperties();
//            props.setCharset("UTF-8");
//            TagNode node = cleaner.clean(templateHtmlFile);
//            new PrettyXmlSerializer(props).writeToStream(node, out);
//
//            // Создаем PDF из подготовленного HTML кода
//            ITextRenderer renderer = new ITextRenderer();
//            renderer.setDocumentFromString(new String(out.toByteArray(), "UTF-8"));
//            renderer.layout();
//            /* заметьте, на этом этапе Вы можете записать PDF документ, скажем, в файл
//             * но раз мы пишем сервлет, который будет возвращать PDF документ,
//             * нам нужен массив байт, который мы отдадим пользователю */
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            renderer.createPDF(outputStream);
//
//            // Завершаем работу
//            renderer.finishPDF();
//            out.flush();
//            out.close();
//        } catch (IOException | DocumentException e) {
//            e.printStackTrace();
//        }
        try {
            Document document = new Document();
            // Создаем writer для записи в pdf
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("/Users/Hans/IdeaProjects/ITIS/jlab/RabitMQCreateDocuments/" + pdfFilePath));
            // Открываем для чтения html страничку
            document.open();
            // Парсим её и записываем в PDF
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(pathTemplateHtmlFile));
            document.close();

            System.out.println("Ваш PDF файл - Создан!");
        } catch (IOException | com.itextpdf.text.DocumentException e) {
            e.printStackTrace();
        }
//        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
//
//        try {
//            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
//            document.open();
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new FileInputStream(templateHtmlFile));
//            document.close();
//        } catch (DocumentException | IOException e) {
//            e.printStackTrace();
//        }

    }


    private void replaceAllInHtml(File templateHtmlFile, Map<String, Object> data) {
        try {

            org.jsoup.nodes.Document doc = Jsoup.parse(templateHtmlFile, "UTF-8");

            String title = doc.html();

            for (Map.Entry e : data.entrySet()) {
                title = title.replaceAll((String) e.getKey(), (String) e.getValue());
            }

            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(templateHtmlFile), "UTF8"));

            out.write(title);
            out.flush();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createHtmlByPdf(File templatePdfFile, String fileName) {
        try {
            PDDocument pdf = PDDocument.load(templatePdfFile);
            Writer output = new PrintWriter("templates/html/" + fileName + ".html", "utf-8");
            new PDFDomTree().writeText(pdf, output);

            output.close();
//            PDFDomTree parser = new PDFDomTree();
//            Document dom = parser.createDOM(pdf);
//            PDFToHTML pdfToHTML = new PDFToHTML();

        } catch (IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

}

//    public void createPdf(Map<String, Object> data, String allFileName, String templateFilePath) {
//        try {
//            File file = new File(templateFilePath);
//            PDDocument document = PDDocument.load(file);
//            PDDocument ansDocument = new PDDocument();
//            ansDocument = replaceAllTextStupid(data, document, ansDocument);
//            ansDocument.save(allFileName);
//            ansDocument.close();
//            document.close();
//        } catch (IOException e) {
//            throw new IllegalArgumentException(e);
//        }
//    }

//    private PDDocument replaceAllTextStupid(Map<String, Object> data, PDDocument from, PDDocument to) {
////        to = clonePDF(from);
////        for (Entry e : data.entrySet()) {
////                to = replaceText(to, "{" + (String) e.getKey() + "}", (String) e.getValue());
////        }
////        to = update(to, data);
//        to = update(from, data);
////        update(data, to);
//        return to;
//    }
//
//    private PDDocument clonePDF(PDDocument from) {
//        PDFCloneUtility pdfCloneUtility = new PDFCloneUtility(from);
//        return pdfCloneUtility.getDestination();
//    }

//    private void update(Map<String, Object> map, PDDocument document) {
//        try {
//            List<PDField> fields = document.getDocumentCatalog().getAcroForm().getFields();
//            for (PDField field : fields) {
//                for (Map.Entry<String, Object> entry : map.entrySet()) {
//                    if (entry.getKey().equals(field.getFullyQualifiedName())) {
//                        field.setValue((String) entry.getValue());
//                        field.setReadOnly(true);
//                    }
//                }
//            }
//        } catch (IOException e) {
//            throw new IllegalArgumentException(e);
//        }
//    }
//}
//        public static PDDocument replaceTextStupid(PDDocument document, String searchString, String replacement) {
//        try {
//            for (PDPage page : document.getPages()) {
//                PDFStreamParser parser = new PDFStreamParser(page);
//                parser.parse();
//                List tokens = parser.getTokens();
//                for (int j = 0; j < tokens.size(); j++) {
//                    Object next = tokens.get(j);
//                    if (next instanceof Operator) {
//                        Operator op = (Operator) next;
//                        if (op.getName().equals("Tj")) {
//                            COSString previous = (COSString) tokens.get(j - 1);
//                            String string = previous.getString();
//                            string = string.replaceFirst(searchString, replacement);
//                            previous.setValue(string.getBytes());
//                        } else if (op.getName().equals("TJ")) {
//                            COSArray previous = (COSArray) tokens.get(j - 1);
//                            for (int k = 0; k < previous.size(); k++) {
//                                Object arrElement = previous.getObject(k);
//                                if (arrElement instanceof COSString) {
//                                    COSString cosString = (COSString) arrElement;
//                                    String string = cosString.getString();
//                                    string = StringUtils.replaceOnce(string, searchString, replacement);
//                                    cosString.setValue(string.getBytes());
//                                }
//                            }
//                        }
//                    }
//                }
//                PDStream updatedStream = new PDStream(document);
//                OutputStream out = updatedStream.createOutputStream(COSName.FLATE_DECODE);
//                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
//                tokenWriter.writeTokens(tokens);
//                // save content
//                page.setContents(updatedStream);
//                out.close();
//            }
//            return document;
//        } catch (IOException e) {
//            throw new IllegalArgumentException(e);
//        }
//    }
//}
//
//    private static PDDocument update(PDDocument document, Map<String, Object> data) {
//
//        try {
//            for (PDPage page : document.getPages()) {
//
//                PDFStreamParser parser = new PDFStreamParser(page);
//
//                parser.parse();
//                List<?> tokens = parser.getTokens();
//                List<Object> newTokens = new ArrayList<Object>();
//                for (int j = 0; j < tokens.size(); j++) {
//                    Object next = tokens.get(j);
//                    if (next instanceof Operator) {
//                        Operator op = (Operator) next;
//
//                        if (op.getName().equals("Tj")) {
//                            COSString previous = (COSString) newTokens.get(newTokens.size() - 1);
//                            String string = previous.getString();
//                            for (Entry e : data.entrySet()) {
//                                string = string.replace("{" + constStringForPdf + (String) e.getKey() + "}", (String) e.getValue());
//                            }
//
//                            COSArray newLink = new COSArray();
//                            newLink.add(new COSString(string));
//                            newTokens.set(newTokens.size() - 1, newLink);
//                        } else if (op.getName().equals("TJ")) {
//                            COSArray previous = (COSArray) newTokens.get(newTokens.size() - 1);
//                            String string = "";
//                            for (int k = 0; k < previous.size(); k++) {
//                                Object arrElement = previous.getObject(k);
//                                if (arrElement instanceof COSString) {
//                                    COSString cosString = (COSString) arrElement;
//                                    String content = cosString.getString();
//                                    string += content;
//                                }
//                            }
//                            for (Entry e : data.entrySet()) {
//                                string = string.replace("{" + constStringForPdf + (String) e.getKey() + "}", (String) e.getValue());
//                            }
//                            COSArray newLink = new COSArray();
//                            newLink.add(new COSString(string));
//                            newTokens.set(newTokens.size() - 1, newLink);
//                        }
//                    }
//                    newTokens.add(next);
//                }
//                PDStream newContents = new PDStream(document);
//                OutputStream out = newContents.createOutputStream(COSName.FLATE_DECODE);
//                ContentStreamWriter writer = new ContentStreamWriter(out);
//                writer.writeTokens(newTokens);
//                out.close();
//
//                // save content
//                page.setContents(newContents);
//            }
//            return document;
//        } catch (IOException e) {
//            throw new IllegalArgumentException(e);
//        }
//    }
//
//}
//    private static PDDocument replaceTextStupid(PDDocument document, String searchString, String replacement) {
//        if (StringUtils.isEmpty(searchString) || StringUtils.isEmpty(replacement)) {
//            return document;
//        }
//
//        try {
//            for (PDPage page : document.getPages()) {
//
//                PDFStreamParser parser = new PDFStreamParser(page);
//
//                parser.parse();
//                List<?> tokens = parser.getTokens();
//
//                for (int j = 0; j < tokens.size(); j++) {
//                    Object next = tokens.get(j);
//                    if (next instanceof Operator) {
//                        Operator op = (Operator) next;
//
//                        String pstring = "";
//                        int prej = 0;
//
//                        if (op.getName().equals("Tj")) {
//                            COSString previous = (COSString) tokens.get(j - 1);
//                            String string = previous.getString();
//                            string = string.replaceFirst(searchString, replacement);
//                            previous.setValue(string.getBytes());
//                        } else if (op.getName().equals("TJ")) {
//                            COSArray previous = (COSArray) tokens.get(j  - 1);
//                            for (int k = 0; k < previous.size(); k++) {
//                                Object arrElement = previous.getObject(k);
//                                if (arrElement instanceof COSString) {
//                                    COSString cosString = (COSString) arrElement;
//                                    String string = cosString.getString();
//
//                                    if (j == prej) {
//                                        pstring += string;
//                                    } else {
//                                        prej = j;
//                                        pstring = string;
//                                    }
//                                }
//                            }
//                            String resultString = pstring.trim().replace(searchString, replacement);
//                            COSString cosString2 = (COSString) previous.getObject(0);
//                            cosString2.setValue(resultString.getBytes());
//
//                            int total = previous.size() - 1;
//                            for (int k = total; k > 0; k--) {
//                                previous.remove(k);
//                            }
//                        }
//                    }
//                }
//                PDStream updatedStream = new PDStream(document);
//                OutputStream out = updatedStream.createOutputStream(COSName.FLATE_DECODE);
//                ContentStreamWriter tokenWriter = new ContentStreamWriter(out);
//                tokenWriter.writeTokens(tokens);
//                out.close();
//                page.setContents(updatedStream);
//            }
//
//            return document;
//        } catch (IOException e) {
//            throw new IllegalArgumentException(e);
//        }
//    }
//}

/*
    private static PDDocument replaceText(PDDocument from, PDDocument to, Map<String, Object> data) {
        try {
            for (PDPage page : from.getPages()) {
                PDPage newPage = new PDPage();
                PDPageContentStream contentStream = new PDPageContentStream(to, page);

                contentStream.beginText();

                PDFStreamParser parser = new PDFStreamParser(page);

                parser.parse();

                List<?> tokens = parser.getTokens();

                for (int j = 0; j < tokens.size(); j++) {
                    Object next = tokens.get(j);
                    if (next instanceof Operator) {
                        Operator op = (Operator) next;

                        String pstring = "";
                        int prej = 0;

//                        if (op.getName().equals("Tj")) {
//                            COSString previous = (COSString) tokens.get(j - 1);
//                            String string = previous.getString();
//                            for (Entry e : data.entrySet()) {
//                                string = string.replaceAll((String) e.getKey(), (String) e.getValue());
//                            }
//                        } else
                        if (op.getName().equals("TJ")) {
                            COSArray previous = (COSArray) tokens.get(j - 1);
                            for (int k = 0; k < previous.size(); k++) {
                                Object arrElement = previous.getObject(k);
                                COSString cosString = (COSString) arrElement;
                                String string = cosString.getString();

                                if (j == prej) {
                                    pstring += string;
                                } else {
                                    prej = j;
                                    pstring = string;
                                }
                            }
                            String checkingString = pstring.trim();
                            for (Entry e : data.entrySet()) {
                                checkingString = checkingString.replaceFirst((String) e.getKey(), (String) e.getValue());
                            }
                            contentStream.showText(checkingString);
                        } else {

                        }
                    }
                }
                newPage.setContents(updatedStream);
            }


            return to;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
*/

