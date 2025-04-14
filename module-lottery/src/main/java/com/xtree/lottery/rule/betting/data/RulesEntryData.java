package com.xtree.lottery.rule.betting.data;

import com.google.gson.annotations.SerializedName;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.vo.MenuMethodsData;

import java.util.List;

/**
 * Created by KAKA on 2024/12/12.
 * Describe:
 */
public class RulesEntryData {

    //{"currentCategory":{"categories":[{"groupName":"后三直选","dy_title":"后三直选","methods":[{"menuid":2331,"methodid":2370,"name":"复式","is_multiple":0,"relationMethods":null,"maxcodecount":0,"selectarea":{"type":"digital","layout":[{"title":"百位","no":"0|1|2|3|4|5|6|7|8|9","place":0,"cols":1},{"title":"十位","no":"0|1|2|3|4|5|6|7|8|9","place":1,"cols":1},{"title":"个位","no":"0|1|2|3|4|5|6|7|8|9","place":2,"cols":1}],"noBigIndex":5,"isButton":true},"show_str":"-,-,X,X,X","code_sp":"","defaultposition":null,"desc":"复式","prize_level":[{"1":"1900.00"}],"prize_group":[{"value":1,"label":"奖金 1900.00-3.0%"},{"value":2,"label":"奖金 1960.00-0.0%"}],"groupName":"后三直选","cateName":"后三码","lotteryId":"14","originalName":"复式","methoddesc":"从个、十、百位各选一个号码组成一注。","methodexample":"投注方案：345；<br>开奖号码：345，<br>即中后三直选一等奖","methodhelp":"从百位、十位、个位中选择一个3位数号码组成一注，所选号码与开奖号码后3位相同，且顺序一致，即为中奖。","description":"复式","money_modes":[{"modeid":1,"name":"元","rate":1},{"modeid":2,"name":"角","rate":0.1},{"modeid":3,"name":"分","rate":0.01},{"modeid":4,"name":"厘","rate":0.001}],"cate_title":"后三码","group_title":"后三直选","current":true},{"menuid":2332,"methodid":2370,"name":"单式","is_multiple":0,"relationMethods":null,"maxcodecount":0,"selectarea":{"type":"input"},"show_str":"X","code_sp":" ","defaultposition":null,"desc":"单式","prize_level":[{"1":"1900.00"}],"prize_group":[{"value":1,"label":"奖金 1900.00-3.0%"},{"value":2,"label":"奖金 1960.00-0.0%"}],"groupName":"后三直选","cateName":"后三码","lotteryId":"14","originalName":"单式","methoddesc":"手动输入号码，至少输入1个三位数号码组成一注。","methodexample":"投注方案：345； 开奖号码：345，即中后三直选一等奖","methodhelp":"手动输入一个3位数号码组成一注，所选号码的百位、十位、个位与开奖号码相同，且顺序一致，即为中奖。","description":"单式","money_modes":[{"modeid":1,"name":"元","rate":1},{"modeid":2,"name":"角","rate":0.1},{"modeid":3,"name":"分","rate":0.01},{"modeid":4,"name":"厘","rate":0.001}],"cate_title":"后三码","group_title":"后三直选","current":false},{"menuid":2333,"methodid":2371,"name":"直选和值","is_multiple":0,"relationMethods":null,"maxcodecount":0,"selectarea":{"type":"digital","layout":[{"title":"直选和值","no":"0|1|2|3|4|5|6|7|8|9|10|11|12|13","place":0,"cols":1},{"title":"","no":"14|15|16|17|18|19|20|21|22|23|24|25|26|27","place":0,"cols":1}],"isButton":false},"show_str":"X","code_sp":",","defaultposition":null,"desc":"直选和值","prize_level":[{"1":"1900.00"}],"prize_group":[{"value":1,"label":"奖金 1900.00-3.0%"},{"value":2,"label":"奖金 1960.00-0.0%"}],"groupName":"后三直选","cateName":"后三码","lotteryId":"14","originalName":"直选和值","methoddesc":"从0-27中任意选择1个或1个以上号码","methodexample":"投注方案：和值1；开奖号码后三位：001,010,100,即中后三直选一等奖","methodhelp":"所选数值等于开奖号码的百位、十位、个位三个数字相加之和，即为中奖。","description":"直选和值","money_modes":[{"modeid":1,"name":"元","rate":1},{"modeid":2,"name":"角","rate":0.1},{"modeid":3,"name":"分","rate":0.01},{"modeid":4,"name":"厘","rate":0.001}],"cate_title":"后三码","group_title":"后三直选","current":false}]},{"groupName":"后三组选","dy_title":"后三组选","methods":[{"menuid":2356,"methodid":2377,"name":"组三","is_multiple":0,"relationMethods":null,"maxcodecount":0,"selectarea":{"type":"digital","layout":[{"title":"组三","no":"0|1|2|3|4|5|6|7|8|9","place":0,"cols":1}],"noBigIndex":5,"isButton":true},"show_str":"X","code_sp":",","defaultposition":null,"desc":"组三","prize_level":[{"1":"633.33"}],"prize_group":[{"value":1,"label":"奖金 633.33-3.0%"},{"value":2,"label":"奖金 653.33-0.0%"}],"groupName":"后三组选","cateName":"后三码","lotteryId":"14","originalName":"组三","methoddesc":"从0-9中任意选择2个或2个以上号码。","methodexample":"投注方案：5,8,8；开奖号码后三位：1个5，2个8 (顺序不限)，即中后三组选三一等奖。","methodhelp":"从0-9中选择2个数字组成两注，所选号码与开奖号码的百位、十位、个位相同，且顺序不限，即为中奖。","description":"组三","money_modes":[{"modeid":1,"name":"元","rate":1},{"modeid":2,"name":"角","rate":0.1},{"modeid":3,"name":"分","rate":0.01},{"modeid":4,"name":"厘","rate":0.001}],"cate_title":"后三码","group_title":"后三组选","current":false},{"menuid":2357,"methodid":2378,"name":"组六","is_multiple":0,"relationMethods":null,"maxcodecount":0,"selectarea":{"type":"digital","layout":[{"title":"组六","no":"0|1|2|3|4|5|6|7|8|9","place":0,"cols":1}],"noBigIndex":5,"isButton":true},"show_str":"X","code_sp":",","defaultposition":null,"desc":"组六","prize_level":[{"2":"316.66"}],"prize_group":[{"value":1,"label":"奖金 316.66-3.0%"},{"value":2,"label":"奖金 326.66-0.0%"}],"groupName":"后三组选","cateName":"后三码","lotteryId":"14","originalName":"组六","methoddesc":"从0-9中任意选择3个或3个以上号码。","methodexample":"投注方案：2,5,8；开奖号码后三位：1个2、1个5、1个8 (顺序不限)，即中后三组选六一等奖。","methodhelp":"从0-9中任意选择3个号码组成一注，所选号码与开奖号码的百位、十位、个位相同，顺序不限，即为中奖。","description":"组六","money_modes":[{"modeid":1,"name":"元","rate":1},{"modeid":2,"name":"角","rate":0.1},{"modeid":3,"name":"分","rate":0.01},{"modeid":4,"name":"厘","rate":0.001}],"cate_title":"后三码","group_title":"后三组选","current":false},{"menuid":2358,"methodid":2379,"name":"混合组选","is_multiple":0,"relationMethods":null,"maxcodecount":0,"selectarea":{"type":"input"},"show_str":"X","code_sp":" ","defaultposition":null,"desc":"混合组选","prize_level":[{"1":"633.33"},{"2":"316.66"}],"prize_group":[{"value":1,"label":"奖金 316.66~633.33 3%"},{"value":2,"label":"奖金 326.66~653.33 0.0%"}],"groupName":"后三组选","cateName":"后三码","lotteryId":"14","originalName":"混合组选","methoddesc":"手动输入号码，至少输入1个三位数号码。","methodexample":"投注方案：分別投注(0,0,1),以及(1,2,3)，开奖号码后三位包括：(1)0,0,1，顺序不限，即中得组三一等奖；或者(2)1,2,3，顺序不限，即中得组六一等奖。","methodhelp":"键盘手动输入购买号码，3个数字为一注，开奖号码的百位、十位、个位符合后三组三或组六均为中奖。","description":"混合组选","money_modes":[{"modeid":1,"name":"元","rate":1},{"modeid":2,"name":"角","rate":0.1},{"modeid":3,"name":"分","rate":0.01},{"modeid":4,"name":"厘","rate":0.001}],"cate_title":"后三码","group_title":"后三组选","current":false},{"menuid":2359,"methodid":2380,"name":"组选和值","is_multiple":0,"relationMethods":null,"maxcodecount":0,"selectarea":{"type":"digital","layout":[{"title":"组选和值","no":"1|2|3|4|5|6|7|8|9|10|11|12|13","place":0,"cols":1},{"title":"","no":"14|15|16|17|18|19|20|21|22|23|24|25|26","place":0,"cols":1}],"isButton":false},"show_str":"X","code_sp":",","defaultposition":null,"desc":"组选和值","prize_level":[{"1":"633.33"},{"2":"316.66"}],"prize_group":[{"value":1,"label":"奖金 316.66~633.33 3%"},{"value":2,"label":"奖金 326.66~653.33 0.0%"}],"groupName":"后三组选","cateName":"后三码","lotteryId":"14","originalName":"组选和值","methoddesc":"从0-9中选择1个号码。","methodexample":"投注方案：和值3；开奖号码后三位：(1)开出003号码，顺序不限，即中后三组选三一等奖；(2)开出012号码，顺序不限，即中后三组选六一等奖","methodhelp":"所选数值等于开奖号码百位、十位、个位三个数字相加之和，即为中奖。","description":"组选和值","money_modes":[{"modeid":1,"name":"元","rate":1},{"modeid":2,"name":"角","rate":0.1},{"modeid":3,"name":"分","rate":0.01},{"modeid":4,"name":"厘","rate":0.001}],"cate_title":"后三码","group_title":"后三组选","current":false}]}],"name":"后三码","flag":"hn5fc"},"currentMethod":{"menuid":2331,"methodid":2370,"name":"复式","is_multiple":0,"relationMethods":null,"maxcodecount":0,"selectarea":{"type":"digital","layout":[{"title":"百位","no":"0|1|2|3|4|5|6|7|8|9","place":0,"cols":1},{"title":"十位","no":"0|1|2|3|4|5|6|7|8|9","place":1,"cols":1},{"title":"个位","no":"0|1|2|3|4|5|6|7|8|9","place":2,"cols":1}],"noBigIndex":5,"isButton":true},"show_str":"-,-,X,X,X","code_sp":"","defaultposition":null,"desc":"复式","prize_level":[{"1":"1900.00"}],"prize_group":[{"value":1,"label":"奖金 1900.00-3.0%"},{"value":2,"label":"奖金 1960.00-0.0%"}],"groupName":"后三直选","cateName":"后三码","lotteryId":"14","originalName":"复式","methoddesc":"从个、十、百位各选一个号码组成一注。","methodexample":"投注方案：345；<br>开奖号码：345，<br>即中后三直选一等奖","methodhelp":"从百位、十位、个位中选择一个3位数号码组成一注，所选号码与开奖号码后3位相同，且顺序一致，即为中奖。","description":"复式","money_modes":[{"modeid":1,"name":"元","rate":1},{"modeid":2,"name":"角","rate":0.1},{"modeid":3,"name":"分","rate":0.01},{"modeid":4,"name":"厘","rate":0.001}],"cate_title":"后三码","group_title":"后三直选","target":true},"bet":{"methodid":2370,"mode":{"modeid":1,"name":"元","rate":1},"prize":2,"times":1,"display":{"prize":"模式:1960.00","mode":"元","rate":1,"times":"1倍","money":0,"num":0,"methodName":"[后三码_复式]","codes":"-,-,3,4,-","prize_group":[{"value":1,"label":"奖金 1900.00-3.0%"},{"value":2,"label":"奖金 1960.00-0.0%"}],"prize_level":[{"1":"1900.00"}],"solo":true,"relationMethods":null,"money_modes":[{"modeid":1,"name":"元","rate":1},{"modeid":2,"name":"角","rate":0.1},{"modeid":3,"name":"分","rate":0.01},{"modeid":4,"name":"厘","rate":0.001}],"singleDesc":null},"codes":[["3"],["4"],["5"]],"submit":{"methodid":2370,"codes":"3|4|","omodel":2,"mode":1,"times":1,"poschoose":null,"menuid":2331,"type":"digital","nums":0,"money":0,"solo":true,"desc":"[后三码_复式]-,-,3,4,-"}},"type":"ssc"}

    /**
     * currentCategory
     */
    @SerializedName("currentCategory")
    private CurrentCategoryDTO currentCategory;
    /**
     * currentMethod
     */
    @SerializedName("currentMethod")
    private CurrentMethodDTO currentMethod;
    /**
     * bet
     */
    @SerializedName("bet")
    private BetDTO bet;
    /**
     * type
     */
    @SerializedName("type")
    private String type;

    public CurrentCategoryDTO getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(CurrentCategoryDTO currentCategory) {
        this.currentCategory = currentCategory;
    }

    public CurrentMethodDTO getCurrentMethod() {
        return currentMethod;
    }

    public void setCurrentMethod(CurrentMethodDTO currentMethod) {
        this.currentMethod = currentMethod;
    }

    public BetDTO getBet() {
        return bet;
    }

    public void setBet(BetDTO bet) {
        this.bet = bet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class CurrentCategoryDTO {
        /**
         * categories
         */
        @SerializedName("categories")
        private List<CategoriesDTO> categories;
        /**
         * name
         */
        @SerializedName("name")
        private String name;
        /**
         * flag
         */
        @SerializedName("flag")
        private String flag;

        public List<CategoriesDTO> getCategories() {
            return categories;
        }

        public void setCategories(List<CategoriesDTO> categories) {
            this.categories = categories;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public static class CategoriesDTO {
            /**
             * groupName
             */
            @SerializedName("groupName")
            private String groupName;
            /**
             * dyTitle
             */
            @SerializedName("dy_title")
            private String dyTitle;
            /**
             * methods
             */
            @SerializedName("methods")
            private List<MethodsDTO> methods;

            public String getGroupName() {
                return groupName;
            }

            public void setGroupName(String groupName) {
                this.groupName = groupName;
            }

            public String getDyTitle() {
                return dyTitle;
            }

            public void setDyTitle(String dyTitle) {
                this.dyTitle = dyTitle;
            }

            public List<MethodsDTO> getMethods() {
                return methods;
            }

            public void setMethods(List<MethodsDTO> methods) {
                this.methods = methods;
            }

            public static class MethodsDTO {
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
                 * selectarea
                 */
                @SerializedName("selectarea")
                private MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO selectarea;
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
                private Object defaultposition;
                /**
                 * desc
                 */
                @SerializedName("desc")
                private String desc;
                /**
                 * prizeLevel
                 */
                @SerializedName("prize_level")
                private List<String> prizeLevel;
                /**
                 * prizeGroup
                 */
                @SerializedName("prize_group")
                private List<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeGroup;
                /**
                 * groupName
                 */
                @SerializedName("groupName")
                private String groupName;
                /**
                 * cateName
                 */
                @SerializedName("cateName")
                private String cateName;
                /**
                 * lotteryId
                 */
                @SerializedName("lotteryId")
                private String lotteryId;
                /**
                 * originalName
                 */
                @SerializedName("originalName")
                private String originalName;
                /**
                 * methoddesc
                 */
                @SerializedName("methoddesc")
                private String methoddesc;
                /**
                 * methodexample
                 */
                @SerializedName("methodexample")
                private String methodexample;
                /**
                 * methodhelp
                 */
                @SerializedName("methodhelp")
                private String methodhelp;
                /**
                 * description
                 */
                @SerializedName("description")
                private String description;
                /**
                 * moneyModes
                 */
                @SerializedName("money_modes")
                private List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO> moneyModes;
                /**
                 * cateTitle
                 */
                @SerializedName("cate_title")
                private String cateTitle;
                /**
                 * groupTitle
                 */
                @SerializedName("group_title")
                private String groupTitle;
                /**
                 * current
                 */
                @SerializedName("current")
                private boolean current;

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

                public MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO getSelectarea() {
                    return selectarea;
                }

                public void setSelectarea(MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO selectarea) {
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

                public Object getDefaultposition() {
                    return defaultposition;
                }

                public void setDefaultposition(Object defaultposition) {
                    this.defaultposition = defaultposition;
                }

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public List<String> getPrizeLevel() {
                    return prizeLevel;
                }

                public void setPrizeLevel(List<String> prizeLevel) {
                    this.prizeLevel = prizeLevel;
                }

                public List<UserMethodsResponse.DataDTO.PrizeGroupDTO> getPrizeGroup() {
                    return prizeGroup;
                }

                public void setPrizeGroup(List<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeGroup) {
                    this.prizeGroup = prizeGroup;
                }

                public String getGroupName() {
                    return groupName;
                }

                public void setGroupName(String groupName) {
                    this.groupName = groupName;
                }

                public String getCateName() {
                    return cateName;
                }

                public void setCateName(String cateName) {
                    this.cateName = cateName;
                }

                public String getLotteryId() {
                    return lotteryId;
                }

                public void setLotteryId(String lotteryId) {
                    this.lotteryId = lotteryId;
                }

                public String getOriginalName() {
                    return originalName;
                }

                public void setOriginalName(String originalName) {
                    this.originalName = originalName;
                }

                public String getMethoddesc() {
                    return methoddesc;
                }

                public void setMethoddesc(String methoddesc) {
                    this.methoddesc = methoddesc;
                }

                public String getMethodexample() {
                    return methodexample;
                }

                public void setMethodexample(String methodexample) {
                    this.methodexample = methodexample;
                }

                public String getMethodhelp() {
                    return methodhelp;
                }

                public void setMethodhelp(String methodhelp) {
                    this.methodhelp = methodhelp;
                }

                public String getDescription() {
                    return description;
                }

                public void setDescription(String description) {
                    this.description = description;
                }

                public List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO> getMoneyModes() {
                    return moneyModes;
                }

                public void setMoneyModes(List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO> moneyModes) {
                    this.moneyModes = moneyModes;
                }

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

                public boolean isCurrent() {
                    return current;
                }

                public void setCurrent(boolean current) {
                    this.current = current;
                }
            }
        }
    }

    public static class CurrentMethodDTO {
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
         * selectarea
         */
        @SerializedName("selectarea")
        private MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO selectarea;
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
        private Object defaultposition;
        /**
         * desc
         */
        @SerializedName("desc")
        private String desc;
        /**
         * prizeLevel
         */
        @SerializedName("prize_level")
        private List<String> prizeLevel;
        /**
         * prizeGroup
         */
        @SerializedName("prize_group")
        private List<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeGroup;
        /**
         * groupName
         */
        @SerializedName("groupName")
        private String groupName;
        /**
         * cateName
         */
        @SerializedName("cateName")
        private String cateName;
        /**
         * lotteryId
         */
        @SerializedName("lotteryId")
        private String lotteryId;
        /**
         * originalName
         */
        @SerializedName("originalName")
        private String originalName;
        /**
         * methoddesc
         */
        @SerializedName("methoddesc")
        private String methoddesc;
        /**
         * methodexample
         */
        @SerializedName("methodexample")
        private String methodexample;
        /**
         * methodhelp
         */
        @SerializedName("methodhelp")
        private String methodhelp;
        /**
         * description
         */
        @SerializedName("description")
        private String description;
        /**
         * moneyModes
         */
        @SerializedName("money_modes")
        private List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO> moneyModes;
        /**
         * cateTitle
         */
        @SerializedName("cate_title")
        private String cateTitle;
        /**
         * groupTitle
         */
        @SerializedName("group_title")
        private String groupTitle;
        /**
         * target
         */
        @SerializedName("target")
        private boolean target;

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

        public MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO getSelectarea() {
            return selectarea;
        }

        public void setSelectarea(MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO selectarea) {
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

        public Object getDefaultposition() {
            return defaultposition;
        }

        public void setDefaultposition(Object defaultposition) {
            this.defaultposition = defaultposition;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<String> getPrizeLevel() {
            return prizeLevel;
        }

        public void setPrizeLevel(List<String> prizeLevel) {
            this.prizeLevel = prizeLevel;
        }

        public List<UserMethodsResponse.DataDTO.PrizeGroupDTO> getPrizeGroup() {
            return prizeGroup;
        }

        public void setPrizeGroup(List<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeGroup) {
            this.prizeGroup = prizeGroup;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public String getLotteryId() {
            return lotteryId;
        }

        public void setLotteryId(String lotteryId) {
            this.lotteryId = lotteryId;
        }

        public String getOriginalName() {
            return originalName;
        }

        public void setOriginalName(String originalName) {
            this.originalName = originalName;
        }

        public String getMethoddesc() {
            return methoddesc;
        }

        public void setMethoddesc(String methoddesc) {
            this.methoddesc = methoddesc;
        }

        public String getMethodexample() {
            return methodexample;
        }

        public void setMethodexample(String methodexample) {
            this.methodexample = methodexample;
        }

        public String getMethodhelp() {
            return methodhelp;
        }

        public void setMethodhelp(String methodhelp) {
            this.methodhelp = methodhelp;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO> getMoneyModes() {
            return moneyModes;
        }

        public void setMoneyModes(List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO> moneyModes) {
            this.moneyModes = moneyModes;
        }

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

        public boolean isTarget() {
            return target;
        }

        public void setTarget(boolean target) {
            this.target = target;
        }
    }

    public static class BetDTO {
        /**
         * methodid
         */
        @SerializedName("methodid")
        private String methodid;
        /**
         * mode
         */
        @SerializedName("mode")
        private ModeDTO mode;
        /**
         * prize
         */
        @SerializedName("prize")
        private int prize;
        /**
         * times
         */
        @SerializedName("times")
        private int times;
        /**
         * display
         */
        @SerializedName("display")
        private DisplayDTO display;
        /**
         * codes
         */
        @SerializedName("codes")
        private Object codes;
        /**
         * submit
         */
        @SerializedName("submit")
        private SubmitDTO submit;

        public String getMethodid() {
            return methodid;
        }

        public void setMethodid(String methodid) {
            this.methodid = methodid;
        }

        public ModeDTO getMode() {
            return mode;
        }

        public void setMode(ModeDTO mode) {
            this.mode = mode;
        }

        public int getPrize() {
            return prize;
        }

        public void setPrize(int prize) {
            this.prize = prize;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public DisplayDTO getDisplay() {
            return display;
        }

        public void setDisplay(DisplayDTO display) {
            this.display = display;
        }

        public Object getCodes() {
            return codes;
        }

        public void setCodes(Object codes) {
            this.codes = codes;
        }

        public SubmitDTO getSubmit() {
            return submit;
        }

        public void setSubmit(SubmitDTO submit) {
            this.submit = submit;
        }

        public static class ModeDTO {
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
            private String rate;

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

            public String getRate() {
                return rate;
            }

            public void setRate(String rate) {
                this.rate = rate;
            }
        }

        public static class DisplayDTO {
            /**
             * prize
             */
            @SerializedName("prize")
            private String prize;
            /**
             * mode
             */
            @SerializedName("mode")
            private String mode;
            /**
             * rate
             */
            @SerializedName("rate")
            private int rate;
            /**
             * times
             */
            @SerializedName("times")
            private String times;
            /**
             * money
             */
            @SerializedName("money")
            private int money;
            /**
             * num
             */
            @SerializedName("num")
            private int num;
            /**
             * methodName
             */
            @SerializedName("methodName")
            private String methodName;
            /**
             * codes
             */
            @SerializedName("codes")
            private String codes;
            /**
             * prizeGroup
             */
            @SerializedName("prize_group")
            private List<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeGroup;
            /**
             * prizeLevel
             */
            @SerializedName("prize_level")
            private List<String> prizeLevel;
            /**
             * solo
             */
            @SerializedName("solo")
            private boolean solo;
            /**
             * relationMethods
             */
            @SerializedName("relationMethods")
            private Object relationMethods;
            /**
             * moneyModes
             */
            @SerializedName("money_modes")
            private List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO> moneyModes;
            /**
             * singleDesc
             */
            @SerializedName("singleDesc")
            private Object singleDesc;

            public String getPrize() {
                return prize;
            }

            public void setPrize(String prize) {
                this.prize = prize;
            }

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }

            public int getRate() {
                return rate;
            }

            public void setRate(int rate) {
                this.rate = rate;
            }

            public String getTimes() {
                return times;
            }

            public void setTimes(String times) {
                this.times = times;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getMethodName() {
                return methodName;
            }

            public void setMethodName(String methodName) {
                this.methodName = methodName;
            }

            public String getCodes() {
                return codes;
            }

            public void setCodes(String codes) {
                this.codes = codes;
            }

            public List<UserMethodsResponse.DataDTO.PrizeGroupDTO> getPrizeGroup() {
                return prizeGroup;
            }

            public void setPrizeGroup(List<UserMethodsResponse.DataDTO.PrizeGroupDTO> prizeGroup) {
                this.prizeGroup = prizeGroup;
            }

            public List<String> getPrizeLevel() {
                return prizeLevel;
            }

            public void setPrizeLevel(List<String> prizeLevel) {
                this.prizeLevel = prizeLevel;
            }

            public boolean isSolo() {
                return solo;
            }

            public void setSolo(boolean solo) {
                this.solo = solo;
            }

            public Object getRelationMethods() {
                return relationMethods;
            }

            public void setRelationMethods(Object relationMethods) {
                this.relationMethods = relationMethods;
            }

            public List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO> getMoneyModes() {
                return moneyModes;
            }

            public void setMoneyModes(List<MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO> moneyModes) {
                this.moneyModes = moneyModes;
            }

            public Object getSingleDesc() {
                return singleDesc;
            }

            public void setSingleDesc(Object singleDesc) {
                this.singleDesc = singleDesc;
            }
        }
    }

    public static class SubmitDTO {
        /**
         * methodid
         */
        @SerializedName("methodid")
        private int methodid;
        /**
         * codes
         */
        @SerializedName("codes")
        private String codes;
        /**
         * omodel
         */
        @SerializedName("omodel")
        private int omodel;
        /**
         * mode
         */
        @SerializedName("mode")
        private int mode;
        /**
         * times
         */
        @SerializedName("times")
        private int times;
        /**
         * poschoose
         */
        @SerializedName("poschoose")
        private Object poschoose;
        /**
         * menuid
         */
        @SerializedName("menuid")
        private int menuid;
        /**
         * type
         */
        @SerializedName("type")
        private String type;
        /**
         * nums
         */
        @SerializedName("nums")
        private int nums;
        /**
         * money
         */
        @SerializedName("money")
        private double money;
        /**
         * solo
         */
        @SerializedName("solo")
        private boolean solo;
        /**
         * desc
         */
        @SerializedName("desc")
        private String desc;

        public int getMethodid() {
            return methodid;
        }

        public void setMethodid(int methodid) {
            this.methodid = methodid;
        }

        public String getCodes() {
            return codes;
        }

        public void setCodes(String codes) {
            this.codes = codes;
        }

        public int getOmodel() {
            return omodel;
        }

        public void setOmodel(int omodel) {
            this.omodel = omodel;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getTimes() {
            return times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public Object getPoschoose() {
            return poschoose;
        }

        public void setPoschoose(Object poschoose) {
            this.poschoose = poschoose;
        }

        public int getMenuid() {
            return menuid;
        }

        public void setMenuid(int menuid) {
            this.menuid = menuid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getNums() {
            return nums;
        }

        public void setNums(int nums) {
            this.nums = nums;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public boolean isSolo() {
            return solo;
        }

        public void setSolo(boolean solo) {
            this.solo = solo;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class RulesResultData {
        private RulesEntryData.BetDTO.DisplayDTO display;
        private List<SubmitDTO> submitDTOS;
        private List<String> messages;

        public RulesEntryData.BetDTO.DisplayDTO getDisplay() {
            return display;
        }

        public void setDisplay(RulesEntryData.BetDTO.DisplayDTO display) {
            this.display = display;
        }

        public List<SubmitDTO> getSubmitDTOS() {
            return submitDTOS;
        }

        public void setSubmitDTOS(List<SubmitDTO> submitDTOS) {
            this.submitDTOS = submitDTOS;
        }

        public List<String> getMessages() {
            return messages;
        }

        public void setMessages(List<String> messages) {
            this.messages = messages;
        }
    }
}
