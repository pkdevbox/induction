
public interface RewriteRule
{
	public View fromURL( URLDef oURL )
	{
	}
}

// issue: how to call a view directly from a URL
//      : option 1: views cannot be called from a URL, only controllers respond to a URL

// issue: how about rewrite rule concept for controllers
//		  : option 1: if views cannot be called from a URL then controllers are the targets
//		  : the rewrite engine dispatches
//		  : transparent reverse rewrite of redirect url, by the redirect() method called from within a controller!!

public class IndustryHomeURL implements RewriteRule
{
	public URLDef toURL( String sIndustryID )
	{
	}


	public View fromURL( URLDef oURL )
	{
		if ( ! oURLDef.getPath().equals( "/industry" ) )
		{
			return null;
		}

		if ( oURLDef.hasParam( "industry_id" ) )
		{
			return ( new IndustryHomeView( oURLDef.getParam( "industry_id" ) ) );
		}
	}
}

public class IndustryHomeView implements View
{
	public IndustryHomeView( String sIndustryID )
	{
	}

	public getTemplateName()
	{
		return "industry_home_page.ftl";
	}

	public getContent()
	{
		return "Hello World!";
	}

	public getMimeType()
	{
		return "text/html";
	}
}


// EOF