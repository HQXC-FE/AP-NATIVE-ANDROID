package com.xtree.lottery.data.source.vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KAKA on 2024/12/24.
 * Describe: 彩票玩法菜单
 */
public class MenuMethodsData {

    /**
     * lotteryid
     */
    @SerializedName(value = "lotteryid", alternate = {"lotteryId"})
    private int lotteryid;
    /**
     * menuid
     */
    @SerializedName(value = "menuid", alternate = {"curmid"})
    private int menuid;
    /**
     * labels
     */
    @SerializedName(value = "labels", alternate = {"data_label"})
    private List<LabelsDTO> labels;

    public int getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(int lotteryid) {
        this.lotteryid = lotteryid;
    }

    public int getMenuid() {
        return menuid;
    }

    public void setMenuid(int menuid) {
        this.menuid = menuid;
    }

    public List<LabelsDTO> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelsDTO> labels) {
        this.labels = labels;
    }

    public static class LabelsDTO {
        /**
         * title
         */
        @SerializedName(value = "title")
        private String title;
        /**
         * dy_title
         */
        @SerializedName(value = "dy_title")
        private String dyTitle;
        /**
         * isdefault
         */
        @SerializedName("isdefault")
        private boolean isdefault;
        /**
         * isnew
         */
        @SerializedName("isnew")
        private boolean isnew;
        /**
         * labels
         */
        @SerializedName(value = "labels", alternate = {"label"})
        private List<Labels1DTO> labels;

        public String getDyTitle() {
            return dyTitle;
        }

        public void setDyTitle(String dyTitle) {
            this.dyTitle = dyTitle;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isIsdefault() {
            return isdefault;
        }

        public void setIsdefault(boolean isdefault) {
            this.isdefault = isdefault;
        }

        public boolean isIsnew() {
            return isnew;
        }

        public void setIsnew(boolean isnew) {
            this.isnew = isnew;
        }

        public List<Labels1DTO> getLabels() {
            return labels;
        }

        public void setLabels(List<Labels1DTO> labels) {
            this.labels = labels;
        }

        public static class Labels1DTO {

            /**
             * 是否应用玩法
             */
            public boolean userPlay = false;

            public boolean isUserPlay() {
                return userPlay;
            }

            public void setUserPlay(boolean userPlay) {
                this.userPlay = userPlay;
            }

            /**
             * title
             */
            @SerializedName(value = "title", alternate = {"gtitle"})
            private String title;

            /**
             * dy_title
             */

            private String dyTitle;
            /**
             * labels
             */
            @SerializedName(value = "labels", alternate = {"label"})
            private List<Labels2DTO> labels;

            public String getDyTitle() {
                return dyTitle;
            }

            public void setDyTitle(String dyTitle) {
                this.dyTitle = dyTitle;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<Labels2DTO> getLabels() {
                return labels;
            }

            public void setLabels(List<Labels2DTO> labels) {
                this.labels = labels;
            }

            public static class Labels2DTO {

                /**
                 * 是否应用玩法
                 */
                public boolean userPlay = false;
                /**
                 * title
                 */
                @SerializedName(value = "cate_title")
                private String cateTitle;
                @SerializedName(value = "group_title")
                private String groupTitle;
                /**
                 * color
                 */
                @SerializedName("color")
                private String color;
                /**
                 * num
                 */
                @SerializedName("num")
                private String num;
                /**
                 * type
                 */
                @SerializedName("type")
                private String type;
                /**
                 * menuid
                 */
                @SerializedName("menuid")
                private String menuid;
                /**
                 * methodid
                 */
                @SerializedName("methodid")
                private String methodid;
                /**
                 * name
                 */
                @SerializedName("name")
                private String name;
                /**
                 * description
                 */
                @SerializedName("description")
                private String description;
                /**
                 * isMultiple
                 */
                @SerializedName("is_multiple")
                private int isMultiple;
                /**
                 * relationMethods
                 */
                @SerializedName("relationMethods")
                private Object relationMethods;
                /**
                 * maxcodecount
                 */
                @SerializedName("maxcodecount")
                private int maxcodecount;
                /**
                 * methoddesc
                 */
                @SerializedName("methoddesc")
                private String methoddesc;
                /**
                 * methodhelp
                 */
                @SerializedName("methodhelp")
                private String methodhelp;
                /**
                 * methodexample
                 */
                @SerializedName("methodexample")
                private String methodexample;
                /**
                 * selectarea
                 */
                @SerializedName("selectarea")
                private SelectareaDTO selectarea;
                /**
                 * showStr
                 */
                @SerializedName("show_str")
                private String showStr;
                /**
                 * codeSp
                 */
                @SerializedName("code_sp")
                private String codeSp;
                /**
                 * defaultposition
                 */
                @SerializedName("defaultposition")
                private String defaultposition;
                /**
                 * moneyModes
                 */
                @SerializedName("money_modes")
                private List<MoneyModesDTO> moneyModes;

                public String getCateTitle() {
                    return cateTitle;
                }

                public void setCateTitle(String cateTitle) {
                    this.cateTitle = cateTitle;
                }

                public String getGroupTitle() {
                    return groupTitle;
                }

                public void setGroupTitle(String groupTitle) {
                    this.groupTitle = groupTitle;
                }

                public String getColor() {
                    return color;
                }

                public void setColor(String color) {
                    this.color = color;
                }

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public boolean isUserPlay() {
                    return userPlay;
                }

                public void setUserPlay(boolean userPlay) {
                    this.userPlay = userPlay;
                }

                public String getMenuid() {
                    return menuid;
                }

                public void setMenuid(String menuid) {
                    this.menuid = menuid;
                }

                public String getMethodid() {
                    return methodid;
                }

                public void setMethodid(String methodid) {
                    this.methodid = methodid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public int getIsMultiple() {
                    return isMultiple;
                }

                public void setIsMultiple(int isMultiple) {
                    this.isMultiple = isMultiple;
                }

                public Object getRelationMethods() {
                    return relationMethods;
                }

                public void setRelationMethods(Object relationMethods) {
                    this.relationMethods = relationMethods;
                }

                public int getMaxcodecount() {
                    return maxcodecount;
                }

                public void setMaxcodecount(int maxcodecount) {
                    this.maxcodecount = maxcodecount;
                }

                public String getMethoddesc() {
                    return methoddesc;
                }

                public void setMethoddesc(String methoddesc) {
                    this.methoddesc = methoddesc;
                }

                public String getMethodhelp() {
                    return methodhelp;
                }

                public void setMethodhelp(String methodhelp) {
                    this.methodhelp = methodhelp;
                }

                public String getMethodexample() {
                    return methodexample;
                }

                public void setMethodexample(String methodexample) {
                    this.methodexample = methodexample;
                }

                public SelectareaDTO getSelectarea() {
                    return selectarea;
                }

                public void setSelectarea(SelectareaDTO selectarea) {
                    this.selectarea = selectarea;
                }

                public String getShowStr() {
                    return showStr;
                }

                public void setShowStr(String showStr) {
                    this.showStr = showStr;
                }

                public String getCodeSp() {
                    return codeSp;
                }

                public void setCodeSp(String codeSp) {
                    this.codeSp = codeSp;
                }

                public String getDefaultposition() {
                    return defaultposition;
                }

                public void setDefaultposition(String defaultposition) {
                    this.defaultposition = defaultposition;
                }

                public List<MoneyModesDTO> getMoneyModes() {
                    return moneyModes;
                }

                public void setMoneyModes(List<MoneyModesDTO> moneyModes) {
                    this.moneyModes = moneyModes;
                }

                public static class SelectareaDTO {
                    /**
                     * type
                     */
                    @SerializedName("type")
                    private String type;

                    /**
                     * type
                     */
                    @SerializedName("originType")
                    private String originType;

                    public String getOriginType() {
                        return originType;
                    }

                    public void setOriginType(String originType) {
                        this.originType = originType;
                    }

                    /**
                     * layout
                     */
                    @SerializedName("layout")
                    private List<LayoutDTO> layout;
                    /**
                     * noBigIndex
                     */
                    @SerializedName("noBigIndex")
                    private int noBigIndex;
                    /**
                     * isButton
                     */
                    @SerializedName("isButton")
                    private boolean isButton;

                    /**
                     * selPosition
                     */
                    @SerializedName("selPosition")
                    private boolean selPosition = false;

                    public boolean isSelPosition() {
                        return selPosition;
                    }

                    public void setSelPosition(boolean selPosition) {
                        this.selPosition = selPosition;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }

                    public List<LayoutDTO> getLayout() {
                        return layout;
                    }

                    public void setLayout(List<LayoutDTO> layout) {
                        this.layout = layout;
                    }

                    public int getNoBigIndex() {
                        return noBigIndex;
                    }

                    public void setNoBigIndex(int noBigIndex) {
                        this.noBigIndex = noBigIndex;
                    }

                    public boolean isIsButton() {
                        return isButton;
                    }

                    public void setIsButton(boolean isButton) {
                        this.isButton = isButton;
                    }

                    public static class LayoutDTO {
                        /**
                         * title
                         */
                        @SerializedName("title")
                        private String title;
                        /**
                         * no
                         */
                        @SerializedName("no")
                        private Object no;
                        /**
                         * place
                         */
                        @SerializedName("place")
                        private int place;
                        /**
                         * cols
                         */
                        @SerializedName("cols")
                        private int cols;
                        /**
                         * minchosen
                         */
                        @SerializedName("minchosen")
                        private int minchosen;

                        public String getTitle() {
                            return title;
                        }

                        public void setTitle(String title) {
                            this.title = title;
                        }

                        public String getNo() {
                            return no.toString();
                        }

                        public void setNo(String no) {
                            this.no = no;
                        }

                        public int getPlace() {
                            return place;
                        }

                        public void setPlace(int place) {
                            this.place = place;
                        }

                        public int getCols() {
                            return cols;
                        }

                        public void setCols(int cols) {
                            this.cols = cols;
                        }

                        public int getMinchosen() {
                            return minchosen;
                        }

                        public void setMinchosen(int minchosen) {
                            this.minchosen = minchosen;
                        }
                    }
                }

                public static class MoneyModesDTO {
                    /**
                     * modeid
                     */
                    @SerializedName("modeid")
                    private int modeid;
                    /**
                     * name
                     */
                    @SerializedName("name")
                    private String name;
                    /**
                     * rate
                     */
                    @SerializedName("rate")
                    private float rate;

                    public int getModeid() {
                        return modeid;
                    }

                    public void setModeid(int modeid) {
                        this.modeid = modeid;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public float getRate() {
                        return rate;
                    }

                    public void setRate(float rate) {
                        this.rate = rate;
                    }
                }
            }
        }

    }
}
