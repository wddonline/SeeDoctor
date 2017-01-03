package org.wdd.app.android.seedoctor.ui.encyclopedia.model;

import org.wdd.app.android.seedoctor.views.index_bar.bean.BaseIndexPinyinBean;

import java.util.List;

/**
 * Created by richard on 1/3/17.
 */

public class Emergency extends BaseIndexPinyinBean {
    public String emeid;
    public String eme;
    public String picurl;
    public List<More> more;

    public String getEmeid() {
        return emeid;
    }

    public void setEmeid(String emeid) {
        this.emeid = emeid;
    }

    public String getEme() {
        return eme;
    }

    public void setEme(String eme) {
        this.eme = eme;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public List<More> getMore() {
        return more;
    }

    public void setMore(List<More> more) {
        this.more = more;
    }

    @Override
    public String getTarget () {
        return eme;
    }

    public class More {
        public String title;
        public String content;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}