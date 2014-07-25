<!doctype html>
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>TSV Importer</title>
  </head>

  <body>

    <h1>TSV Importer</h1>
	<div id="mainContent">
		<form action="/importer/inilike" method="post" enctype="multipart/form-data">
			<input name="data" type="file"/>
			<button>Submit</button>
		</form>
		<a href="/importer/xml">Download all data as XML</a>
	</div>
  </body>
</html>