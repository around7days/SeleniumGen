package ${pageBean.packageNm};

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

/**
 * ${pageBean.classNm}クラス
 */
public class ${pageBean.classNm} <#if pageBean.extendsClassNm?has_content> extends ${pageBean.extendsClassNm} </#if>{

    /** ロガー */
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(${pageBean.classNm}.class);

    /* 項目変数宣言 ------------------------------------------------------------------------------------------------- */
<#list pageBean.itemList as itemBean>
    /** ${itemBean.item} */
    @FindBy(${itemBean.findBy} = "${itemBean.findByVal}")
    @CacheLookup
  <#if itemBean.findByCnt?has_content>
    private List<WebElement> ${itemBean.item};
  <#else>
    private WebElement ${itemBean.item};
  </#if>

</#list>

    /* 共通メソッド宣言 --------------------------------------------------------------------------------------------- */
    /**
     * PageFactoryを使用してWebElementをマッピングする
     * @return Page
     */
    public ${pageBean.classNm} initialize() {
        return PageFactory.initElements(driver, this.getClass());
    }

<#list pageBean.itemList as itemBean>
  <#-- ####### 共通 -->
  <#if itemBean.findByCnt?has_content>
  public List<WebElement> ${itemBean.item}() {
      return this.${itemBean.item};
  }
  <#else>
  public WebElement ${itemBean.item}() {
      return this.${itemBean.item};
  }
  </#if>
</#list>

    /* IE操作メソッド ----------------------------------------------------------------------------------------------- */
<#list pageBean.itemList as itemBean>
  <#-- ####### sendkeys -->
  <#if itemBean.operateSendKeys?has_content>
    <#if itemBean.findByCnt?has_content>
      /**
       * ${itemBean.item}に値を入力します。
       * @param sendKeys
       */
      public void ${itemBean.item}sendkeys(String sendKeys) {
          ${itemBean.item}sendkeys(sendKeys, 0);
      }
      /**
       * ${itemBean.item}に値を入力します。
       * @param sendKeys
       * @param index
       */
      public void ${itemBean.item}sendkeys(String sendKeys, int index) {
          this.${itemBean.item}.get(index).clear();
          this.${itemBean.item}.get(index).sendKeys(sendKeys);
      }
    <#else>
      /**
       * ${itemBean.item}に値を入力します。
       * @param sendKeys
       */
      public void ${itemBean.item}sendkeys(String sendKeys) {
          this.${itemBean.item}.clear();
          this.${itemBean.item}.sendKeys(sendKeys);
      }
    </#if>
  </#if>

  <#-- ####### getValue -->
  <#if itemBean.operateGetValue?has_content>
    /**
     * ${itemBean.item}の値を取得します。
     * @return value
     */
    public String ${itemBean.item}getValue() {
        return this.${itemBean.item}.getAttribute("value");
    }
  </#if>

  <#-- ####### getText -->
  <#if itemBean.operateGetText?has_content>
    /**
     * ${itemBean.item}の値を取得します。
     * @return text
     */
    public String ${itemBean.item}getText() {
        return this.${itemBean.item}.getText();
    }
  </#if>

  <#-- ####### click -->
  <#if itemBean.operateClick?has_content>
    <#if itemBean.findByCnt?has_content>
      /**
       * ${itemBean.item}をクリックします。
       */
      public void ${itemBean.item}click() {
          ${itemBean.item}click(0);
      }
      /**
       * ${itemBean.item}をクリックします。
       * @param index
       */
      public void ${itemBean.item}click(int index) {
          this.${itemBean.item}.get(index).click();
      }
    <#else>
      /**
       * ${itemBean.item}をクリックします。
       */
      public void ${itemBean.item}click() {
          this.${itemBean.item}.click();
      }
    </#if>
  </#if>

  <#-- ####### selectByIndex -->
  <#if itemBean.operateSelectIndex?has_content>
    /**
     * ${itemBean.item}を選択します。
     * @param index
     */
    public void ${itemBean.item}selectByIndex(int index) {
        new Select(this.${itemBean.item}).selectByIndex(index);
    }
  </#if>

  <#-- ####### selectByValue -->
  <#if itemBean.operateSelectValue?has_content>
    /**
     * ${itemBean.item}を選択します。
     * @param value
     */
    public void ${itemBean.item}selectByValue(String value) {
        new Select(this.${itemBean.item}).selectByValue(value);
    }
  </#if>

  <#-- ####### selectByVisibleText -->
  <#if itemBean.operateSelectText?has_content>
    /**
     * ${itemBean.item} を選択します。
     * @param visibleText
     */
    public void ${itemBean.item}selectByVisibleText(String visibleText) {
        new Select(this.${itemBean.item}).selectByVisibleText(visibleText);
    }
  </#if>

  <#-- ####### frameChange -->
  <#if itemBean.operateFrameChange?has_content>
    /**
     * 対象Frameを ${itemBean.item} に変更します。
     */
    public WebDriver ${itemBean.item}frameChange() {
        return driver.switchTo().frame(${itemBean.item});
    }
  </#if>

</#list>
}
