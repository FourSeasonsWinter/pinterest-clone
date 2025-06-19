import SearchBar from "../../components/search-bar/SearchBar";
import './SearchPage.css'

export default function SearchPage() {
  return (
    <main>
      <SearchBar />
      <div className="recommendations-banner">
        <p>Ideias em alta</p>
        <h2>Festas Juninas</h2>
      </div>
    </main>
  );
}