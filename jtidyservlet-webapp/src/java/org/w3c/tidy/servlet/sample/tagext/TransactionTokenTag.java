package org.w3c.tidy.servlet.sample.tagext;
/*
 * Created on 30.08.2004 by vlads
 */
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.TokenProcessor;

/**
    This tag will produce this HTML code
    <input type="hidden" name="..."
 */

public class TransactionTokenTag extends TagSupport 
{

    private String verify;
    private String message;

    /**
     * Logger.
     */
    private Log log = LogFactory.getLog(TransactionTokenTag.class);
    
    public TransactionTokenTag() 
    {

    }

    public int doStartTag() throws JspException
    {
        try
        {
            if ((verify == null) || (verify.equalsIgnoreCase("false")))
            {
                pageContext.getOut().println(_renderInput());

                return SKIP_BODY;
            }
            else
            {
                if (verifyUpdateRequest((HttpServletRequest) pageContext.getRequest()))
                {
                    return EVAL_BODY_INCLUDE;
                }
                else
                {
                    if (message != null)
                    {
                        pageContext.getOut().println(message);
                    }
                    return SKIP_BODY;
                }
            }
        }
        catch (IOException e)
        {
            log.error("TransactionToken write", e);
            throw new JspException(e.getMessage());
        }
    }

    public int doEndTag() throws JspException
    {
        return EVAL_PAGE;
    }

    public void release()
    {
        verify = null;
        message = null;
        
    }
    
	private boolean verifyUpdateRequest(HttpServletRequest request)
    {
        //	Retrieve the transaction token included in this request
        if (request == null)
        {
            return TokenProcessor.getInstance().isTokenValid(request, true);
        }
        String token = request.getParameter(Constants.TOKEN_KEY);
        if (token != null)
        {
            // reset the token after checking it
            String session_token = (String) request.getSession().getAttribute(Globals.TRANSACTION_TOKEN_KEY);
            if (log.isDebugEnabled())
            {
                log.debug("request token:" + token);
                log.debug("session token:" + session_token);
            }
            if (!TokenProcessor.getInstance().isTokenValid(request, true))
            {
                // you can't use that form more than once.
                return false;
            }
            request.setAttribute(Constants.TOKEN_KEY, session_token);
            return true;
        }
        else
        {
            log.debug("no token in request");
            return false;
        }
    }
	
    private String _renderInput() 
    {
        StringBuffer results = new StringBuffer(1024);
        HttpSession session = pageContext.getSession();

        if (session != null) 
        {
            String token =
                (String) session.getAttribute(Globals.TRANSACTION_TOKEN_KEY);

            if (token == null) 
            {
                TokenProcessor.getInstance().saveToken((HttpServletRequest)pageContext.getRequest());
                token = (String) session.getAttribute(Globals.TRANSACTION_TOKEN_KEY);
            }

            if (token != null) 
            {
                results.append("<input type=\"hidden\" name=\"");
                results.append(Constants.TOKEN_KEY);
                results.append("\" value=\"");
                results.append(token);
                results.append("\">\n");
            }
        }
        return results.toString();
    }
    
    /**
     * @param verify The verify to set.
     */
    public void setVerify(String verify)
    {
        this.verify = verify;
    }
    /**
     * @param message The message to set.
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
}
