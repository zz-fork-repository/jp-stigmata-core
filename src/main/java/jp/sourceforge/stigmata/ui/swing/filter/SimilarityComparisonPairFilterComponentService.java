package jp.sourceforge.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jp.sourceforge.stigmata.ComparisonPairFilter;
import jp.sourceforge.stigmata.filter.SimilarityComparisonPairFilter;
import jp.sourceforge.stigmata.filter.SimilarityComparisonPairFilterService;
import jp.sourceforge.stigmata.spi.ComparisonPairFilterSpi;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;

/**
 * 
 * @author Haruaki TAMADA
 */
public class SimilarityComparisonPairFilterComponentService extends AbstractComparisonPairFilterComponentService{

    @Override
    public ComparisonPairFilterPane createComponent(StigmataFrame frame, ComparisonPairFilterSpi service){
        return new Pane(frame, service);
    }

    @Override
    public String getFilterName(){
        return "similarity";
    }

    @Override
    public ComparisonPairFilterSpi getComparisonPairFilterService(){
        return new SimilarityComparisonPairFilterService();
    }

    private static class Pane extends ComparisonPairFilterPane{
        private static final long serialVersionUID = 8912037614500713027L;
        private ComparisonPairFilterSpi service;
        private JComboBox criterionType;
        private JTextField threshold;

        public Pane(StigmataFrame frame, ComparisonPairFilterSpi service){
            super(frame);
            this.service = service;
            initLayouts();
        }

        @Override
        public String[] getErrors(){
            List<String> errors = new ArrayList<String>();
            if(threshold.getText().trim().equals("")){
                errors.add(getMessages().get("error.empty.threshold"));
            }
            try{
                double v = Double.parseDouble(threshold.getText());
                if(v < 0d){
                    errors.add(getMessages().format("error.negative.value", v));
                }
                else if(v > 1.0d){
                    errors.add(getMessages().format("error.over.range", "0-1"));
                }
            } catch(NumberFormatException e){
                errors.add(getMessages().format("error.invalid.format.double", threshold.getText()));
            }

            return errors.toArray(new String[errors.size()]);
        }

        @Override
        public ComparisonPairFilter getFilter(){
            try{
                SimilarityComparisonPairFilter filter = new SimilarityComparisonPairFilter(service);
                filter.setThreshold(Double.parseDouble(threshold.getText()));
                filter.setCriterion(getCriterion((String)criterionType.getSelectedItem()));

                return filter;
            } catch(Exception e){
            }
            return null;
        }

        @Override
        public void resetComponents(){
            threshold.setText("");
            criterionType.setSelectedIndex(0);
        }

        @Override
        public void setFilter(ComparisonPairFilter filter){
            if(filter != null){
                SimilarityComparisonPairFilter sf = (SimilarityComparisonPairFilter)filter;
                criterionType.setSelectedItem(getDisplayCriterion(sf.getCriterion()));
                threshold.setText(Double.toString(sf.getThreshold()));
            }
            else{
                resetComponents();
            }
        }

        private void initLayouts(){
            JLabel label = new JLabel(getMessages().get("filter.similarity.label"));
            threshold = new JTextField();
            criterionType = createCriteriaBox(SimilarityComparisonPairFilter.getValidCriteria());

            setLayout(new GridLayout(3, 1));
            add(label);
            add(criterionType);
            add(threshold);

            threshold.getDocument().addDocumentListener(new DocumentListener(){
                @Override
                public void changedUpdate(DocumentEvent e){
                }

                @Override
                public void insertUpdate(DocumentEvent e){
                }

                @Override
                public void removeUpdate(DocumentEvent e){
                }
            });
        }
    };
}
