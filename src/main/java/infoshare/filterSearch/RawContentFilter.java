package infoshare.filterSearch;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import infoshare.domain.Content;
import infoshare.domain.RawContent;
import infoshare.services.RawContent.Impl.RawContentServiceImpl;
import infoshare.services.RawContent.RawContentService;
import infoshare.services.category.CategoryService;
import infoshare.services.category.Impl.CategoryServiceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by codex on 2015/07/14.
 */
public class RawContentFilter {
    private RawContentService rawContentService = new RawContentServiceImpl();
    private CategoryService categoryService = new CategoryServiceImpl();
    public TextField field = new TextField();

    public RawContentFilter() {
        getField();
    }
    public synchronized List<RawContent> findAll(String stringFilter) {
        DateFormat formatter = new SimpleDateFormat("dd - MMMMMMM - yyyy");
        ArrayList arrayList = new ArrayList();
        String cat;
        for (RawContent rawContent : rawContentService.findAll()
                .stream().filter(cont ->cont.getState().equalsIgnoreCase("active"))
                .collect(Collectors.toList())
                .stream().filter(cont ->cont.getStatus().equalsIgnoreCase("raw"))
                .collect(Collectors.toList())
                ) {
            if(!rawContent.getCategory().equalsIgnoreCase("uncategorized"))
                cat = categoryService.find(rawContent.getCategory().toString()).getName().toLowerCase();
                else cat = rawContent.getCategory().toString().toLowerCase();

            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || rawContent.getTitle().toString().toLowerCase()
                        .contains(stringFilter.toLowerCase())
                        ||cat.contains(stringFilter.toLowerCase())
                        || rawContent.getCreator().toString().toLowerCase()
                        .contains(stringFilter.toLowerCase())
                        || rawContent.getSource().toString().toLowerCase()
                        .contains(stringFilter.toLowerCase())
                        ||formatter.format(rawContent.getDateCreated()).toString().toLowerCase()
                        .contains(stringFilter.toLowerCase());

                if (passesFilter) {
                    arrayList.add(rawContent);
                }
            } catch (Exception ex) {
                Logger.getLogger(ex.getLocalizedMessage());
            }
        }

        return arrayList;
    }
    private TextField getField(){
        field.setInputPrompt("Filter Content ...");
        field.setWidth("260px");
        field.setIcon(FontAwesome.FILTER);
        field.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        field.addStyleName(ValoTheme.TEXTFIELD_SMALL);
        return field;
    }


}
